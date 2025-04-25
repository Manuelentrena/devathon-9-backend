package com.devathon.griffindor_backend.models;

import java.util.*;

import com.devathon.griffindor_backend.enums.RoomVisibility;
import lombok.Getter;

@Getter
public class Room {

    public static final int MAX_PLAYERS = 2;

    private final UUID roomId;
    private final Map<String, PlayerRound> players = new HashMap<>();
    private final RoomVisibility visibility;
    private int currentRound = 1;

    public Room(RoomVisibility visibility) {
        this.roomId = UUID.randomUUID();
        this.visibility = visibility;
    }

    public Set<String> getPlayerIds() {
        return new HashSet<>(players.keySet());
    }

    public boolean addPlayer(String playerId) {
        if (isFull() || players.containsKey(playerId))
            return false;
        players.put(playerId, new PlayerRound());
        return true;
    }

    public boolean removePlayer(String playerId) {
        return players.remove(playerId) != null;
    }

    public boolean isFull() {
        return players.size() >= MAX_PLAYERS;
    }

    public boolean containsPlayer(String playerId) {
        return players.containsKey(playerId);
    }

    public boolean isEmpty() { return players.isEmpty();}

    public PlayerRound getPlayerRound(String playerId) {
        return players.get(playerId);
    }

    public void incrementCurrentRound() {
        currentRound++;
    }

    public String getOponentOf(String sessionId) {
        if (!players.containsKey(sessionId)) {
            throw new IllegalArgumentException("The session ID is not part of this room.");
        }
    
        return players.keySet().stream()
                .filter(id -> !id.equals(sessionId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No oponent found in the room"));
    }
    
}
