package com.devathon.griffindor_backend.events;

import com.devathon.griffindor_backend.dtos.PlayerCountDto;
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

    public PlayerCountDto toDto() {
        return new PlayerCountDto(playerCount);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(toDto());
    }

}