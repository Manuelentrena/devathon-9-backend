package com.devathon.griffindor_backend.listeners;

import com.devathon.griffindor_backend.Queues.RoomReadyQueue;
import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.dtos.RoomIdResponseDto;
import com.devathon.griffindor_backend.events.RoomReadyEvent;

import io.github.springwolf.core.asyncapi.annotations.AsyncMessage;
import io.github.springwolf.core.asyncapi.annotations.AsyncOperation;
import io.github.springwolf.core.asyncapi.annotations.AsyncPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.net.http.WebSocket;

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

    private MessageHeaders buildHeaders(String sessionId) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
        accessor.setSessionId(sessionId);
        accessor.setLeaveMutable(true);
        return accessor.getMessageHeaders();
    }

    @Scheduled(fixedRate = 500)
    @AsyncPublisher(operation = @AsyncOperation(
            channelName = WebSocketRoutes.USER_QUEUE_DUEL,
            description = "Notify players when the room is ready",
            payloadType = RoomIdResponseDto.class,
            message = @AsyncMessage(
                    messageId = "room-ready-id",
                    name = "RoomIdResponseDto",
                    description = "Payload model for room ready event, including room_id"
            )
    ))
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

    
}
