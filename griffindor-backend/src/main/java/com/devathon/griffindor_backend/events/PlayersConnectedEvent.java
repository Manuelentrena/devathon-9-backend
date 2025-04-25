package com.devathon.griffindor_backend.events;

import com.devathon.griffindor_backend.dtos.PlayerConnectedDto;

public class PlayersConnectedEvent {

    private final String destination;
    private final String sessionId;

    public PlayersConnectedEvent(String destination, String sessionId) {
        this.destination = destination;
        this.sessionId = sessionId;
    }

    public String getDestination() {
        return destination;
    }

    public String getSessionId() {
        return sessionId;
    }

    public PlayerConnectedDto toDto(int activePlayers) {
        return new PlayerConnectedDto(activePlayers);
    }
}