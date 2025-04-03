package com.devathon.griffindor_backend.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.dtos.PlayerRegisterDto;
import com.devathon.griffindor_backend.dtos.SucessResponseDto;
import com.devathon.griffindor_backend.services.PlayerService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class PlayersOnlineController {

    private final PlayerService playerService;

    public PlayersOnlineController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @MessageMapping(WebSocketRoutes.REGISTER_USER)
    @SendToUser(WebSocketRoutes.QUEUE_REGISTER_USER)
    public SucessResponseDto registerPlayer(@Payload PlayerRegisterDto playerRegisterDto,
           @Payload SimpMessageHeaderAccessor headerAccessor) {

        if (!playerService.existsBySessionId(playerRegisterDto.session_id())) {
            throw new IllegalArgumentException("Session ID not registered");
        }

        playerService.updatePlayerInfo(
                playerRegisterDto.session_id(),
                playerRegisterDto.name(),
                playerRegisterDto.house());

        return new SucessResponseDto("SUCCESS", "Jugador actualizado correctamente.");
    }

}
