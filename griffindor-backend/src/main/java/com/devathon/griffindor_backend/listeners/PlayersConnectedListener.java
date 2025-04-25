package com.devathon.griffindor_backend.listeners;

import com.devathon.griffindor_backend.Queues.PlayersConnectedQueue;
import com.devathon.griffindor_backend.events.PlayersConnectedEvent;
import com.devathon.griffindor_backend.services.PlayerService;
import com.devathon.griffindor_backend.dtos.PlayerConnectedDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PlayersConnectedListener {

    @Autowired
    private PlayersConnectedQueue eventQueue;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private PlayerService playerService;

    @Scheduled(fixedRate = 1000)
    public void procesarEventos() {
        PlayersConnectedEvent event = eventQueue.pollLatest();
        if (event != null) {
            int activePlayers = playerService.getNumPlayerConnected();
            PlayerConnectedDto dto = event.toDto(activePlayers);
            messagingTemplate.convertAndSend(event.getDestination(), dto);
        }
    }
}
