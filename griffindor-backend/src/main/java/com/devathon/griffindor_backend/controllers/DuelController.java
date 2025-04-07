package com.devathon.griffindor_backend.controllers;

import com.devathon.griffindor_backend.Queues.DuelResultQueue;
import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.dtos.CastSpellsDto;
import com.devathon.griffindor_backend.dtos.DuelResultDto;
import com.devathon.griffindor_backend.events.DuelResultEvent;
import com.devathon.griffindor_backend.services.ErrorService;
import com.devathon.griffindor_backend.services.RoomService;
import com.devathon.griffindor_backend.services.SpellService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class DuelController {

    private final SpellService spellService;
    private final ErrorService errorService;
    private final RoomService roomService;
    private final DuelResultQueue duelResultQueue;

    @MessageMapping(WebSocketRoutes.CAST_SPELL)
    public void duelResult(@Payload CastSpellsDto castSpells, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();

        final int MAX_SPELLS = 2;

        if (sessionId == null) {
            errorService.sendErrorToSession("unknown", "SESSION_ERROR", "Session ID is null");
            return;
        }

        if (!roomService.roomExist(castSpells.roomId())) {
            errorService.sendErrorToSession(sessionId, "ROOM_ERROR", "Room not found");
            return;
        }

        if (castSpells.spells() == null || castSpells.spells().size() != MAX_SPELLS) {
            errorService.sendErrorToSession(sessionId, "SPELL_ERROR", "You must cast exactly " + MAX_SPELLS + " spells");
            return;
        }

        for (UUID spell : castSpells.spells()) {
            if (!spellService.spellExist(spell)) {
                errorService.sendErrorToSession(sessionId, "SPELL_ERROR", "Spell not found");
                return;
            }
        }

        // TODO: implement logic to resolve a dynamic duel based on the list of spells
        UUID spellId1 = castSpells.spells().get(0);
        UUID spellId2 = castSpells.spells().get(1);

        DuelResultDto duelResult = spellService.resolveDuel(spellId1, spellId2);
        duelResultQueue.enqueue(new DuelResultEvent(castSpells.roomId(), duelResult, WebSocketRoutes.QUEUE_DUEL_RESULT));
    }

}
