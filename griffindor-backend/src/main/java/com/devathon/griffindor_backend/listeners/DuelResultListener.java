package com.devathon.griffindor_backend.listeners;

import com.devathon.griffindor_backend.Queues.DuelResultQueue;
import com.devathon.griffindor_backend.events.DuelResultEvent;
import com.devathon.griffindor_backend.models.Room;
import com.devathon.griffindor_backend.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DuelResultListener {

    private final SimpMessagingTemplate messagingTemplate;
    private final DuelResultQueue duelResultQueue;
    private final RoomService roomService;

    @Scheduled(fixedRate = 500)
    public void processQueue() {
        DuelResultEvent event;
        while ((event = duelResultQueue.poll()) != null) {
            Room room = roomService.getOneRoom(event.getRoomId());
            for (String playerId : room.getPlayerIds()) {
                messagingTemplate.convertAndSendToUser(
                        playerId,
                        event.getDestination(),
                        event.getDuelResult(),
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
