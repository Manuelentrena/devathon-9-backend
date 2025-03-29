package com.devathon.griffindor_backend.listeners;

import com.devathon.griffindor_backend.events.PlayerCountUpdateEvent;
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
    public void handlePlayerCountUpdateEvent(PlayerCountUpdateEvent event) {
        messagingTemplate.convertAndSend(event.getDestination(), event.toDto());
    }
}
