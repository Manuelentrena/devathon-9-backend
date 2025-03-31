package com.devathon.griffindor_backend.listeners;

import com.devathon.griffindor_backend.events.PlayerConnectedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class PlayerCountUpdateListener {

    private final SimpMessageSendingOperations messagingTemplate;

    public PlayerCountUpdateListener(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handlePlayerCountUpdateEvent(PlayerConnectedEvent event) {
        messagingTemplate.convertAndSend(event.getDestination(), event.toDto());
    }
}
