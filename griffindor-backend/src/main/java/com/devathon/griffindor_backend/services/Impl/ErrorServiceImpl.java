package com.devathon.griffindor_backend.services.Impl;

import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.dtos.ErrorResponseDto;
import com.devathon.griffindor_backend.services.ErrorService;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

@Service
public class ErrorServiceImpl implements ErrorService {

    private final SimpMessagingTemplate messagingTemplate;

    public ErrorServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendErrorToSession(String sessionId, String type, String message) {
        System.out.println("🔁 sendErrorToSession");
        System.out.println("🔁 type:" + type);
        System.out.println("🔁 message:" + message);
        System.out.println("🔁 sessionId:" + sessionId);
        ErrorResponseDto error = new ErrorResponseDto(type, message);
        messagingTemplate.convertAndSendToUser(
                sessionId,
                WebSocketRoutes.QUEUE_ERRORS,
                error,
                buildHeaders(sessionId));
    }

    private MessageHeaders buildHeaders(String sessionId) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
        accessor.setSessionId(sessionId);
        accessor.setLeaveMutable(true);
        return accessor.getMessageHeaders();
    }
}
