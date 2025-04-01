package com.devathon.griffindor_backend.events;

import java.util.Collection;

import com.devathon.griffindor_backend.dtos.PlayerDto;
import com.devathon.griffindor_backend.models.Player;

public class PlayersListEvent {
    private final String destination;
    private final String sessionId;

    public PlayersListEvent(String destination, String sessionId) {
        this.destination = destination;
        this.sessionId = sessionId;
    }

    public String getDestination() {
        return destination;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Collection<PlayerDto> toDto(Collection<Player> players) {
        return players.stream()
                .map(PlayerDto::new)
                .toList();
    }
}
