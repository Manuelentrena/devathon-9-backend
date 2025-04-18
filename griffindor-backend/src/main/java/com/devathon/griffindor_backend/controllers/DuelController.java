package com.devathon.griffindor_backend.controllers;

import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.dtos.PlayerSpellDto;
import com.devathon.griffindor_backend.dtos.RoundRequestDto;
import com.devathon.griffindor_backend.dtos.RoundResponseDto;
import com.devathon.griffindor_backend.dtos.RoundResult;
import com.devathon.griffindor_backend.models.PlayerRound;
import com.devathon.griffindor_backend.models.Room;
import com.devathon.griffindor_backend.services.ErrorService;
import com.devathon.griffindor_backend.services.RoomService;
import com.devathon.griffindor_backend.services.SpellService;

import io.github.springwolf.core.asyncapi.annotations.AsyncMessage;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DuelController {

    private final SpellService spellService;
    private final ErrorService errorService;
    private final RoomService roomService;
    private final SimpMessagingTemplate messagingTemplate;

    private MessageHeaders buildHeaders(String sessionId) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
        accessor.setSessionId(sessionId);
        accessor.setLeaveMutable(true);
        return accessor.getMessageHeaders();
    }

    @MessageMapping(WebSocketRoutes.SUBMIT_ROUND)
    @SendToUser(WebSocketRoutes.QUEUE_ROUND_RESULT)
    @AsyncPublisher(operation = @AsyncOperation(
            channelName = WebSocketRoutes.QUEUE_ROUND_RESULT,
            description = "Notify players of the round result after all players have submitted their spells.",
            payloadType = RoundResponseDto.class,
            message = @AsyncMessage(
                    messageId = "round-result-id",
                    name = "RoundResponseDto",
                    description = "Payload model for round results, including players' spells and current round status."
            )
    ))
    public void handleRound(@DestinationVariable UUID roomId, @Valid @Payload RoundRequestDto roundRequest, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();

        if (sessionId == null) {
            errorService.sendErrorToSession("unknown", "SESSION_ERROR", "Session ID is null");
            return;
        }

        if (!roomService.roomExist(roomId)) {
            errorService.sendErrorToSession(sessionId, "ROOM_NOT_FOUND", "Room not found");
            return;
        }

        if (!roomService.belongsRoom(roomId, sessionId)) {
            errorService.sendErrorToSession(sessionId, "PLAYER_NOT_FOUND", "Player not found in this room");
            return;
        }

        if (!spellService.spellExist(roundRequest.spellId())) {
            errorService.sendErrorToSession(sessionId, "SPELL_NOT_FOUND", "Spell not found");
            return;
        }

        Room room = roomService.getOneRoom(roomId);

        PlayerRound playerRound = room.getPlayers().get(sessionId);

        playerRound.addSpell(roundRequest.spellId());

        long playersReady = room.getPlayers().values().stream()
                .filter(p -> p.getSpells().size() == room.getCurrentRound())
                .count();

        if (playersReady == Room.MAX_PLAYERS) {
            RoundResult roundResult = spellService.resolveRound(room);

            String playerWinnerDuel = room.getPlayers().entrySet().stream()
                    .filter(entry -> entry.getValue().getRoundsWon() >= 3)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

            boolean gameOver = playerWinnerDuel != null;

            Set<PlayerSpellDto> playerDtos = room.getPlayers().entrySet().stream()
                    .map(entry -> {
                        String playerId = entry.getKey();
                        PlayerRound pr = entry.getValue();
                        UUID spellUsed = pr.getSpellForRound(room.getCurrentRound());
                        return new PlayerSpellDto(playerId, spellUsed, pr.getRoundsWon());
                    })
                    .collect(Collectors.toSet());

            RoundResponseDto roundResponse = new RoundResponseDto(
                    room.getCurrentRound(),
                    gameOver,
                    roundResult,
                    playerDtos
            );

            for (String playerId : room.getPlayerIds()) {
                messagingTemplate.convertAndSendToUser(
                        playerId,
                        WebSocketRoutes.QUEUE_ROUND_RESULT,
                        roundResponse,
                        buildHeaders(playerId)
                );
            }
            // todo: delete room when gameOver true?
            room.incrementCurrentRound();
        }

    }


}
