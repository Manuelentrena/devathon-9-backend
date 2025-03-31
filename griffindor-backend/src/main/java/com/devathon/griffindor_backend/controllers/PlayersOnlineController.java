package com.devathon.griffindor_backend.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.dtos.PlayerRegisterDto;
import com.devathon.griffindor_backend.dtos.SucessResponseDto;
import com.devathon.griffindor_backend.services.PlayerService;

@Controller
public class PlayersOnlineController {

    private final PlayerService playerService;

    public PlayersOnlineController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @MessageMapping(WebSocketRoutes.NUM_PLAYERS)
    public void sendPlayerCount(String message, SimpMessageHeaderAccessor headerAccessor) {
        playerService.publishPlayerConnected();
    }

    @MessageMapping(WebSocketRoutes.REGISTER_USER)
    @SendToUser(WebSocketRoutes.QUEUE_REGISTER_USER)
    // EVENT: list user
    public SucessResponseDto registerPlayer(PlayerRegisterDto playerRegisterDto,
            SimpMessageHeaderAccessor headerAccessor) {

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
