package com.devathon.griffindor_backend.services.Impl;

import com.devathon.griffindor_backend.Queues.PlayersConnectedQueue;
import com.devathon.griffindor_backend.Queues.PlayersListQueue;
import com.devathon.griffindor_backend.config.WebSocketRoutes;
import com.devathon.griffindor_backend.enums.PlayerSessionState;
import com.devathon.griffindor_backend.events.PlayersConnectedEvent;
import com.devathon.griffindor_backend.events.PlayersListEvent;
import com.devathon.griffindor_backend.models.Player;
import com.devathon.griffindor_backend.services.PlayerService;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayersConnectedQueue playersConnectedQueue;
    private final PlayersListQueue playersListQueue;
    private final ConcurrentHashMap<String, Player> players = new ConcurrentHashMap<>();

    public PlayerServiceImpl(PlayersConnectedQueue playersConnectedQueue, PlayersListQueue playersListQueue) {
        this.playersConnectedQueue = playersConnectedQueue;
        this.playersListQueue = playersListQueue;
    }

    /* SETTER */

    public void addPlayer(String sessionId, String name, String house, PlayerSessionState sessionState,
            String tokenId) {
        Player player = new Player(sessionId, name, house, sessionState, tokenId);
        players.put(sessionId, player);
        enqueuePlayersConnectedEvent(sessionId);
        enqueuePlayersListEvent(sessionId);
    }

    public void addToken(String sessionId, String tokenId) {
        Player player = players.get(sessionId);
        if (player != null) {
            player.setTokenId(tokenId);
        }
    }

    public void removePlayer(String sessionId) {
        players.remove(sessionId);
        enqueuePlayersConnectedEvent(sessionId);
        enqueuePlayersListEvent(sessionId);
    }

    public Player updatePlayerInfo(String sessionId, String name, String house) {
        Player player = players.get(sessionId);
        if (player != null) {
            player.setName(name);
            player.setHouse(house);
        }
        enqueuePlayersConnectedEvent(sessionId);
        enqueuePlayersListEvent(sessionId);

        return player;
    }

    public void updatePlayerSessionState(String sessionId, PlayerSessionState state) {
        Player player = players.get(sessionId);
        if (player != null) {
            player.setSessionState(state);
        }
        enqueuePlayersConnectedEvent(sessionId);
        enqueuePlayersListEvent(sessionId);
    }

    public void reconnectFromPreviousSession(String oldSessionId, String newSessionId, String newToken) {
        Player player = players.remove(oldSessionId);

        if (player != null) {
            // TODO: validate player not repeat
            player.setSessionId(newSessionId);
            player.setTokenId(newToken);
            player.setSessionState(PlayerSessionState.CONNECT);
            players.put(newSessionId, player);
        }
        enqueuePlayersConnectedEvent(newSessionId);
        enqueuePlayersListEvent(newSessionId);
    }

    /* GETTER */

    public int getAllPlayerCount() {
        return players.size();
    }

    public int getNumPlayerConnected() {
        return (int) players.values().stream()
                .filter(player -> player.getSessionStatus() == PlayerSessionState.CONNECT)
                .count();
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

    public Collection<Player> getAllPlayersExcept(String sessionIdToExclude) {
        return players.values().stream()
                .filter(player -> !player.getSessionId().equals(sessionIdToExclude))
                .toList();
    }

    public boolean existsBySessionId(String sessionId) {
        return players.containsKey(sessionId);
    }

    /* EVENTS */

    public void enqueuePlayersConnectedEvent(String sessionId) {
        playersConnectedQueue.enqueue(new PlayersConnectedEvent(WebSocketRoutes.TOPIC_NUM_PLAYERS, sessionId));
    }

    public void enqueuePlayersListEvent(String sessionId) {
        playersListQueue.enqueue(new PlayersListEvent(WebSocketRoutes.QUEUE_LIST_PLAYERS, sessionId));
    }

}
