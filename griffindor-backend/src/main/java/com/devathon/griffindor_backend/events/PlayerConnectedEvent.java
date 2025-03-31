package com.devathon.griffindor_backend.events;

import com.devathon.griffindor_backend.dtos.PlayerConnectedDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PlayerConnectedEvent {

    private final int playerCount;
    private final String destination;

    public PlayerConnectedEvent(int playerCount, String destination) {
        this.playerCount = playerCount;
        this.destination = destination;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public String getDestination() {
        return destination;
    }

    public PlayerConnectedDto toDto() {
        return new PlayerConnectedDto(playerCount);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(toDto());
    }

}