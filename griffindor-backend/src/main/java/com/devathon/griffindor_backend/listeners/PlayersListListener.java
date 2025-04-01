package com.devathon.griffindor_backend.listeners;

import com.devathon.griffindor_backend.Queues.PlayersListQueue;
import com.devathon.griffindor_backend.events.PlayersListEvent;
import com.devathon.griffindor_backend.models.Player;
import com.devathon.griffindor_backend.services.PlayerService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PlayersListListener {

    @Autowired
    private PlayersListQueue eventQueue;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private PlayerService playerService;

    @Scheduled(fixedRate = 1000)
    public void procesarEventos() {
        PlayersListEvent event = eventQueue.pollLatest();
        if (event != null) {
            Collection<Player> allPlayers = playerService.getAllPlayers();

            for (Player player : allPlayers) {
                String playerSessionId = player.getSessionId();
                Collection<Player> otherPlayers = playerService.getAllPlayersExcept(playerSessionId);

                messagingTemplate.convertAndSendToUser(
                        playerSessionId,
                        event.getDestination(),
                        event.toDto(otherPlayers),
                        buildHeaders(playerSessionId));
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
