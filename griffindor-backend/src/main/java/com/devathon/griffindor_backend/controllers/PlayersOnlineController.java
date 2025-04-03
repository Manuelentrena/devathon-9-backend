package com.devathon.griffindor_backend.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.dtos.PlayerDto;
import com.devathon.griffindor_backend.dtos.PlayerRegisterDto;
import com.devathon.griffindor_backend.models.Player;
import com.devathon.griffindor_backend.services.ErrorService;
import com.devathon.griffindor_backend.services.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Controller
@Slf4j
public class PlayersOnlineController {

    private final PlayerService playerService;
    private final ErrorService errorService;

    @MessageMapping(WebSocketRoutes.REGISTER_USER)
    @SendToUser(WebSocketRoutes.QUEUE_REGISTER_USER)
    public PlayerDto registerPlayer(@Payload PlayerRegisterDto playerRegisterDto,
            SimpMessageHeaderAccessor headerAccessor) {

        String sessionId = headerAccessor.getSessionId();

        if (sessionId == null) {
            errorService.sendErrorToSession("unknown", "SESSION_ERROR", "Session ID is null");
            return null;
        }

        if (playerRegisterDto.name() == null || playerRegisterDto.name().isBlank()) {
            errorService.sendErrorToSession(sessionId, "VALIDATION_ERROR", "Name is required");
            return null;
        }

        if (playerRegisterDto.house() == null || playerRegisterDto.house().isBlank()) {
            errorService.sendErrorToSession(sessionId, "VALIDATION_ERROR", "House is required");
            return null;
        }

        if (!playerService.existsBySessionId(sessionId)) {
            errorService.sendErrorToSession(sessionId, "PLAYER_NOT_FOUND", "Session ID not registered");
            return null;
        }

        Player updatedPlayer = playerService.updatePlayerInfo(
                sessionId,
                playerRegisterDto.name(),
                playerRegisterDto.house());

        return new PlayerDto(updatedPlayer);
    }

}
