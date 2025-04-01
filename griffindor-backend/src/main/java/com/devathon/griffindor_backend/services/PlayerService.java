package com.devathon.griffindor_backend.services;

import com.devathon.griffindor_backend.enums.PlayerSessionState;
import com.devathon.griffindor_backend.models.Player;
import java.util.Collection;

public interface PlayerService {

    void addPlayer(String sessionId, String name, String house, PlayerSessionState sessionState, String tokenId);

    void addToken(String sessionId, String tokenId);

    void removePlayer(String sessionId);

    void updatePlayerInfo(String sessionId, String name, String house);

    void updatePlayerSessionState(String sessionId, PlayerSessionState state);

    int getAllPlayerCount();

    int getNumPlayerConnected();

    void printAllPlayers();

    Collection<Player> getAllPlayers();

    Collection<Player> getAllPlayersExcept(String sessionIdToExclude);

    boolean existsBySessionId(String sessionId);

    void reconnectFromPreviousSession(String oldSessionId, String newSessionId, String newToken);

    void enqueuePlayersConnectedEvent(String sessionId);

    void enqueuePlayersListEvent(String sessionId);

}
