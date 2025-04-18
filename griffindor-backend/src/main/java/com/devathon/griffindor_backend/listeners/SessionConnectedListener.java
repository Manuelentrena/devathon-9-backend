package com.devathon.griffindor_backend.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.enums.PlayerSessionState;
import com.devathon.griffindor_backend.services.PlayerService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SessionConnectedListener {

  private final PlayerService playerService;

  @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        String sessionId = getSessionIdFromEvent(event);
        playerService.addPlayer(sessionId, null, null, PlayerSessionState.CONNECT, null);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = getSessionIdFromEvent(event);
        playerService.updatePlayerSessionState(sessionId, PlayerSessionState.DISCONNECT);
    }

    @EventListener
    public void handleSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = headerAccessor.getSessionId();
        String destination = headerAccessor.getDestination();

        if (WebSocketRoutes.TOPIC_NUM_PLAYERS.equals(destination)) {
            playerService.enqueuePlayersConnectedEvent(sessionId);

        }
        if (WebSocketRoutes.USER_QUEUE_LIST_PLAYERS.equals(destination)) {
            playerService.enqueuePlayersListEvent(sessionId);
        }
    }

  private String getSessionIdFromEvent(SessionConnectedEvent event) {
    return StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
  }

  private String getSessionIdFromEvent(SessionDisconnectEvent event) {
    return StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
  }

}
