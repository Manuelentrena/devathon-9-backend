package com.devathon.griffindor_backend.listeners;

import com.devathon.griffindor_backend.Queues.RoomReadyQueue;
import com.devathon.griffindor_backend.events.RoomReadyEvent;
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

    @Scheduled(fixedRate = 500)
    public void processQueue() {
        RoomReadyEvent event;
        while ((event = roomReadyQueue.poll()) != null) {
            for (String playerId : event.getRoom().getPlayerIds()) {
                messagingTemplate.convertAndSendToUser(
                        playerId,
                        event.getDestination(),
                        event.toDto(),
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
