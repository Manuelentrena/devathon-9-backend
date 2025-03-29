package com.devathon.griffindor_backend.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.services.PlayerService;

@Controller
public class PlayersOnlineController {

    private final PlayerService playerService;

    public PlayersOnlineController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @MessageMapping(WebSocketRoutes.NUM_PLAYERS)
    public void sendPlayerCount(String message, SimpMessageHeaderAccessor headerAccessor) {
        playerService.publishPlayerCountUpdate();
    }

}
