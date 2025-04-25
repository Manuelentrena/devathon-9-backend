package com.devathon.griffindor_backend.controllers;

import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.dtos.PlayerSpellDto;
import com.devathon.griffindor_backend.dtos.RoundRequestDto;
import com.devathon.griffindor_backend.dtos.RoundResponseDto;
import com.devathon.griffindor_backend.dtos.RoundResult;
import com.devathon.griffindor_backend.enums.PlayerSessionState;
import com.devathon.griffindor_backend.models.PlayerRound;
import com.devathon.griffindor_backend.models.Room;
import com.devathon.griffindor_backend.services.ErrorService;
import com.devathon.griffindor_backend.services.PlayerService;
import com.devathon.griffindor_backend.services.RoomService;
import com.devathon.griffindor_backend.services.SpellService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DuelController {

    private final SpellService spellService;
    private final ErrorService errorService;
    private final RoomService roomService;
    private final PlayerService playerService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping(WebSocketRoutes.SUBMIT_ROUND)
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

        if (roundRequest.spellId() != null && !spellService.spellExist(roundRequest.spellId())) {
            errorService.sendErrorToSession(sessionId, "SPELL_NOT_FOUND", "Spell not found");
            return;
        }

        // Get the room and current player round
        Room room = roomService.getOneRoom(roomId);
        PlayerRound playerRound = room.getPlayers().get(sessionId);

        synchronized (room) {
            // Check if the player has already cast a spell for the current round
            if (room.getCurrentRound() == playerRound.getSpells().size()) {
                errorService.sendErrorToSession(sessionId, "SPELL_ALREADY_SENT", "You already cast a spell this round");
                return;
            }

            // Store the selected spell for the round
            playerRound.addSpell(roundRequest.spellId());

            // Count how many players have submitted spells for the current round
            long playersReady = room.getPlayers().values().stream()
                    .filter(p -> p.getSpells().size() == room.getCurrentRound())
                    .count();

            // If all players have submitted, resolve the round
            if (playersReady == Room.MAX_PLAYERS) {
                RoundResult roundResult = spellService.resolveRound(room);

                // Check if any player has won the duel (3 rounds)
                String playerWinnerDuel = room.getPlayers().entrySet().stream()
                        .filter(entry -> entry.getValue().getRoundsWon() >= 3)
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse(null);

                boolean gameOver = playerWinnerDuel != null;

                // Build DTOs for each player
                Set<PlayerSpellDto> playerDtos = room.getPlayers().entrySet().stream()
                        .map(entry -> {
                            String playerId = entry.getKey();
                            PlayerRound pr = entry.getValue();
                            UUID spellUsed = pr.getSpellForRound(room.getCurrentRound());
                            return new PlayerSpellDto(playerId, spellUsed, pr.getRoundsWon());
                        })
                        .collect(Collectors.toSet());

                // Build round response with current round info
                RoundResponseDto roundResponse = new RoundResponseDto(
                        room.getCurrentRound(),
                        gameOver,
                        roundResult,
                        playerDtos
                );

                // Send round result to each player in the room
                for (String playerId : room.getPlayerIds()) {
                    messagingTemplate.convertAndSendToUser(
                            playerId,
                            WebSocketRoutes.QUEUE_ROUND_RESULT,
                            roundResponse,
                            buildHeaders(playerId)
                    );
                }

                // If the game is over, delete the room
                if (gameOver) {
                    room.getPlayerIds()
                            .forEach(playerId -> playerService.updatePlayerSessionState(playerId, PlayerSessionState.CONNECT));
                    roomService.deleteRoom(roomId);
                    return;
                }

                // Advance to the next round
                room.incrementCurrentRound();
            }
        }
    }

    private MessageHeaders buildHeaders(String sessionId) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
        accessor.setSessionId(sessionId);
        accessor.setLeaveMutable(true);
        return accessor.getMessageHeaders();
    }
}
