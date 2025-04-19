package com.devathon.griffindor_backend.listeners;

import com.devathon.griffindor_backend.Queues.RoomReadyQueue;
import com.devathon.griffindor_backend.dtos.PlayerDto;
import com.devathon.griffindor_backend.events.RoomReadyEvent;
import com.devathon.griffindor_backend.models.Player;
import com.devathon.griffindor_backend.services.PlayerService;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomReadyListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final RoomReadyQueue roomReadyQueue;
    private final PlayerService playerService;

    @Scheduled(fixedRate = 500)
    public void processQueue() {
        RoomReadyEvent event;
        while ((event = roomReadyQueue.poll()) != null) {
            for (String playerId : event.getRoom().getPlayerIds()) {
                String oponentId = event.getRoom().getOponentOf(playerId);
                Player oponent = playerService.getPlayerBySessionId(oponentId);
 
                messagingTemplate.convertAndSendToUser(
                        playerId,
                        event.getDestination(),
                        event.toDto(new PlayerDto(oponent)),
                        buildHeaders(playerId));
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
