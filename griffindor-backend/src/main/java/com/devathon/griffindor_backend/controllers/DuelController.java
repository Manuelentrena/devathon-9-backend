package com.devathon.griffindor_backend.controllers;

import com.devathon.griffindor_backend.Queues.DuelResultQueue;
import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.dtos.RoundRequestDto;
import com.devathon.griffindor_backend.dtos.RoundResponseDto;
import com.devathon.griffindor_backend.events.DuelResultEvent;
import com.devathon.griffindor_backend.models.Room;
import com.devathon.griffindor_backend.services.ErrorService;
import com.devathon.griffindor_backend.services.RoomService;
import com.devathon.griffindor_backend.services.SpellService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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

    @MessageMapping(WebSocketRoutes.SUBMIT_ROUND)
    public void round(@DestinationVariable UUID roomId, @Payload RoundRequestDto roundRequest, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();

        if (sessionId == null) {
            errorService.sendErrorToSession("unknown", "SESSION_ERROR", "Session ID is null");
            return;
        }

        if (!roomService.roomExist(roomId)) {
            errorService.sendErrorToSession(sessionId, "ROOM_ERROR", "Room not found");
            return;
        }

        if (!spellService.spellExist(roundRequest.player1().spellId()) ||
                !spellService.spellExist(roundRequest.player2().spellId())) {
            errorService.sendErrorToSession(sessionId, "SPELL_ERROR", "One or both spells are invalid");
            return;
        }

        if (!roomService.belongsRoom(roomId, roundRequest.player1().sessionId()) ||
                !roomService.belongsRoom(roomId, roundRequest.player2().sessionId())) {
            errorService.sendErrorToSession(sessionId, "PLAYER_ERROR", "One or both players not in this room");
            return;
        }

        Room room = roomService.getOneRoom(roomId);

        RoundResponseDto duelResult = spellService.resolveRound(room, roundRequest);
        duelResultQueue.enqueue(new DuelResultEvent(roomId, duelResult, WebSocketRoutes.QUEUE_ROUND_RESULT));
    }

}
