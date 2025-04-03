package com.devathon.griffindor_backend.controllers;

import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.dtos.TokenIdResponseDto;
import com.devathon.griffindor_backend.enums.PlayerSessionState;
import com.devathon.griffindor_backend.services.ErrorService;
import com.devathon.griffindor_backend.services.PlayerService;
import com.devathon.griffindor_backend.utils.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@RequiredArgsConstructor
@Controller
@Slf4j
public class GameSessionController {

    private final PlayerService playerService;
    private final Jwt jwt;
    private final ErrorService errorService;

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

    @MessageMapping(WebSocketRoutes.TOKEN_ID)
    @SendToUser(WebSocketRoutes.QUEUE_TOKEN_ID)
    public TokenIdResponseDto sendToken(String message, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String token = headerAccessor.getFirstNativeHeader("token_id");

        if (token != null && !token.isBlank()) {
            try {
                String oldSessionId = jwt.validateAndGetUser(token);

                if (playerService.existsBySessionId(oldSessionId)) {
                    String newToken = jwt.generateToken(sessionId);
                    playerService.reconnectFromPreviousSession(oldSessionId, sessionId,
                            newToken);
                    // TODO: actualizar la room si existe donde estuviera el anterior jugador
                    return new TokenIdResponseDto(newToken);
                } else {
                    errorService.sendErrorToSession(
                            sessionId,
                            "SESSION_NOT_FOUND",
                            "The original seccion doesn´t exist.");
                }

            } catch (Exception e) {
                errorService.sendErrorToSession(
                        sessionId,
                        "TOKEN_INVALID",
                        "Invalid o expired Token: " + e.getMessage());
                return null;
            }
        }

        String newToken = jwt.generateToken(sessionId);
        playerService.addToken(sessionId, newToken);
        return new TokenIdResponseDto(newToken);
    }

    private String getSessionIdFromEvent(SessionConnectedEvent event) {
        return StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
    }

    private String getSessionIdFromEvent(SessionDisconnectEvent event) {
        return StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
    }
}
