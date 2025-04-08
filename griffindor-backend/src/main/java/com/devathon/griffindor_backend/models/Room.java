package com.devathon.griffindor_backend.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.devathon.griffindor_backend.enums.RoomVisibility;

public class Room {

    public static final int MAX_PLAYERS = 2;

    private final UUID roomId;
    private final Set<String> playerIds = new HashSet<>();
    private final RoomVisibility visibility;
    private final String name;

    public Room(RoomVisibility visibility, String name) {
        this.roomId = UUID.randomUUID();
        this.visibility = visibility;
        this.name = name;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public Set<String> getPlayerIds() {
        return playerIds;
    }

    public RoomVisibility getVisibility() {
        return visibility;
    }

    public boolean addPlayer(String playerId) {
        if (isFull())
            return false;
        return playerIds.add(playerId);
    }

    public boolean removePlayer(String playerId) {
        return playerIds.remove(playerId);
    }

    public boolean isFull() {
        return playerIds.size() >= MAX_PLAYERS;
    }

    public boolean containsPlayer(String playerId) {
        return playerIds.contains(playerId);
    }

    public boolean isEmpty() {
        return playerIds.isEmpty();
    }

    public String getName() {
        return name;
    }
}
