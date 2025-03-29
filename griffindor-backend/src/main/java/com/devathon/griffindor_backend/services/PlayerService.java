package com.devathon.griffindor_backend.services;

import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.enums.PlayerSessionState;
import com.devathon.griffindor_backend.events.PlayerCountUpdateEvent;
import com.devathon.griffindor_backend.models.Player;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final ApplicationEventPublisher eventPublisher;
    private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    public PlayerService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void addPlayer(String sessionId, String name, String house, PlayerSessionState sessionState) {
        Player player = new Player(sessionId, name, house, sessionState);
        players.put(sessionId, player);
        publishPlayerCountUpdate();
    }

    public void removePlayer(String sessionId) {
        players.remove(sessionId);
        publishPlayerCountUpdate();
    }

    public void updatePlayerInfo(String sessionId, String name, String house) {
        Player player = players.get(sessionId);
        if (player != null) {
            player.setName(name);
            player.setHouse(house);
        }
    }

    public void updatePlayerSessionState(String sessionId, PlayerSessionState state) {
        Player player = players.get(sessionId);
        if (player != null) {
            player.setSessionState(state);
        }
    }

    public int getActivePlayerCount() {
        return players.size();
    }

    public void publishPlayerCountUpdate() {
        eventPublisher.publishEvent(new PlayerCountUpdateEvent(players.size(), WebSocketRoutes.TOPIC_NUM_PLAYERS));
    }

    public void printAllPlayers() {
        System.out.println("📑 Players: ");
        for (Player player : players.values()) {
            System.out.println("Player ID: " + player.getSessionId() + ", Name: "
                    + player.getName() + ", House: "
                    + player.getHouse() + ", SessionStatus: "
                    + player.getSessionStatus());
        }
    }

    public Collection<Player> getAllPlayers() {
        return players.values();
    }
}
