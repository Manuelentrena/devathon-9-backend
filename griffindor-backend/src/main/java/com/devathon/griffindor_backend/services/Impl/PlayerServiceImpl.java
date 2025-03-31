package com.devathon.griffindor_backend.services.Impl;

import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.enums.PlayerSessionState;
import com.devathon.griffindor_backend.events.PlayerConnectedEvent;
import com.devathon.griffindor_backend.models.Player;
import com.devathon.griffindor_backend.services.PlayerService;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final ApplicationEventPublisher eventPublisher;
    private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    public PlayerServiceImpl(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void addPlayer(String sessionId, String name, String house, PlayerSessionState sessionState,
            String tokenId) {
        Player player = new Player(sessionId, name, house, sessionState, tokenId);
        players.put(sessionId, player);
        publishPlayerConnected();
    }

    public void addToken(String sessionId, String tokenId) {
        Player player = players.get(sessionId);
        if (player != null) {
            player.setTokenId(tokenId);
        }
    }

    public void removePlayer(String sessionId) {
        players.remove(sessionId);
        publishPlayerConnected();
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

    public void publishPlayerConnected() {
        int connectedPlayers = (int) players.values().stream()
                .filter(player -> player.getSessionStatus() == PlayerSessionState.CONNECT)
                .count();
        eventPublisher.publishEvent(new PlayerConnectedEvent(
                connectedPlayers, WebSocketRoutes.TOPIC_NUM_PLAYERS));
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

    public boolean existsBySessionId(String sessionId) {
        return players.containsKey(sessionId);
    }

    public void reconnectFromPreviousSession(String oldSessionId, String newSessionId, String newToken) {
        Player player = players.remove(oldSessionId);

        if (player != null) {
            // TODO: validate player not repeat
            player.setSessionId(newSessionId);
            player.setTokenId(newToken);
            players.put(newSessionId, player);
        }
        // EVENT: publishPlayerConnected();
    }
}
