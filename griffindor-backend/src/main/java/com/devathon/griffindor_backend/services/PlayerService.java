package com.devathon.griffindor_backend.services;

import com.devathon.griffindor_backend.enums.PlayerSessionState;
import com.devathon.griffindor_backend.models.Player;
import java.util.Collection;

public interface PlayerService {

    /*
     * ============================
     * SETTERS
     * ============================
     */

    /**
     * Adds a new player to the system.
     *
     * @param sessionId    Session identifier of the player
     * @param name         Player's name
     * @param house        Player's house
     * @param sessionState Initial session state of the player
     * @param tokenId      Associated token ID
     */
    void addPlayer(String sessionId, String name, String house, PlayerSessionState sessionState, String tokenId);

    /**
     * Updates the token for an existing player.
     *
     * @param sessionId Session identifier of the player
     * @param tokenId   New token ID to assign
     */
    void addToken(String sessionId, String tokenId);

    /**
     * Removes a player from the system.
     *
     * @param sessionId Session identifier of the player to remove
     */
    void removePlayer(String sessionId);

    /**
     * Updates the name and house of a player.
     *
     * @param sessionId Session identifier
     * @param name      New name
     * @param house     New house
     * @return The updated player
     */
    Player updatePlayerInfo(String sessionId, String name, String house);

    /**
     * Updates the session state of a player.
     *
     * @param sessionId Session identifier
     * @param state     New session state
     */
    void updatePlayerSessionState(String sessionId, PlayerSessionState state);

    /*
     * ============================
     * GETTERS
     * ============================
     */

    /**
     * Checks whether the player has both name and house registered.
     *
     * @param sessionId Session identifier
     * @return true if both name and house are present
     */
    boolean hasPlayerInfo(String sessionId);

    /**
     * Returns the total number of players in memory.
     *
     * @return Player count
     */
    int getAllPlayerCount();

    /**
     * Returns the number of players currently connected (not disconnected).
     *
     * @return Connected player count
     */
    int getNumPlayerConnected();

    /**
     * Prints all players to the console/log.
     */
    void printAllPlayers();

    /**
     * Returns a collection of all players.
     *
     * @return All players
     */
    Collection<Player> getAllPlayers();

    /**
     * Returns all players except the one with the specified session ID.
     *
     * @param sessionIdToExclude Session ID to exclude
     * @return Collection of other players
     */
    Collection<Player> getAllPlayersExcept(String sessionIdToExclude);

    /**
     * Checks if a player exists by their session ID.
     *
     * @param sessionId Session identifier
     * @return true if the player exists
     */
    boolean existsBySessionId(String sessionId);

    /**
     * Reconnects a player by transferring state from a previous session.
     *
     * @param oldSessionId Previous session ID
     * @param newSessionId New session ID
     * @param newToken     Token to assign to the new session
     */
    void reconnectFromPreviousSession(String oldSessionId, String newSessionId, String newToken);

    /*
     * ============================
     * EVENTS
     * ============================
     */

    /**
     * Enqueues an event to notify that a player has connected.
     *
     * @param sessionId Session identifier of the connected player
     */
    void enqueuePlayersConnectedEvent(String sessionId);

    /**
     * Enqueues an event to notify about the current player list.
     *
     * @param sessionId Session identifier requesting the list
     */
    void enqueuePlayersListEvent(String sessionId);

}
