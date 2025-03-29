package com.devathon.griffindor_backend.events;

import com.devathon.griffindor_backend.dtos.PlayerCountResponseDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PlayerCountUpdateEvent {

    private final int playerCount;
    private final String destination;

    public PlayerCountUpdateEvent(int playerCount, String destination) {
        this.playerCount = playerCount;
        this.destination = destination;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public String getDestination() {
        return destination;
    }

    public PlayerCountResponseDto toDto() {
        return new PlayerCountResponseDto(playerCount);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(toDto());
    }

}