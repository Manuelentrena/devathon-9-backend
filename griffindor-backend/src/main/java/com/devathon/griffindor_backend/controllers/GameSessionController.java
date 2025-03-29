package com.devathon.griffindor_backend.controllers;

import com.devathon.griffindor_backend.enums.PlayerSessionState;
import com.devathon.griffindor_backend.services.PlayerService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
public class GameSessionController {

    private final PlayerService playerService;

    public GameSessionController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        String sessionId = getSessionIdFromEvent(event);
        playerService.addPlayer(sessionId, null, null, PlayerSessionState.ACTIVE);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = getSessionIdFromEvent(event);
        playerService.removePlayer(sessionId);
    }

    private String getSessionIdFromEvent(SessionConnectedEvent event) {
        return StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
    }

    private String getSessionIdFromEvent(SessionDisconnectEvent event) {
        return StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
    }
}
