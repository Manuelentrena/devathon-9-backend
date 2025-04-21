package com.devathon.griffindor_backend.services;

import com.devathon.griffindor_backend.enums.RoomVisibility;
import com.devathon.griffindor_backend.models.Room;

import java.util.UUID;

public interface RoomService {

    /*
     * ============================
     * SETTERS
     * ============================
     */

    /**
     * Creates a new room with the given visibility and returns it.
     */
    Room createRoom(RoomVisibility visibility);

    /**
     * Adds a player to the specified room.
     *
     * @param roomId   UUID of the room
     * @param playerId ID of the player
     * @return true if the player was successfully added
     */
    boolean joinRoom(UUID roomId, String playerId);

    /**
     * Removes a player from the specified room.
     *
     * @param roomId   UUID of the room
     * @param playerId ID of the player
     * @return true if the player was successfully removed
     */
    boolean takeOutRoom(UUID roomId, String playerId);

    /*
     * ============================
     * GETTERS
     * ============================
     */

    /**
     * Checks whether the specified room is full.
     *
     * @param roomId UUID of the room
     * @return true if the room is full
     */
    boolean isFull(UUID roomId);

    /**
     * Checks whether a player belongs to the specified room.
     *
     * @param roomId   UUID of the room
     * @param playerId playerId ID of the player
     * @return true if the player is part of the room
     */
    boolean belongsRoom(UUID roomId, String playerId);

    /**
     * Checks whether the specified room exists.
     *
     * @param roomId UUID of the room
     * @return true if the room exists
     */
    boolean roomExist(UUID roomId);

    /**
     * Retrieves a room by its ID.
     *
     * @param roomId UUID of the room
     * @return the corresponding Room object
     * @throws IllegalArgumentException if the room does not exist
     */
    Room getOneRoom(UUID roomId);

    /**
     * Deletes the room with the specified ID.
     *
     * @param roomId UUID of the room to delete
     */
    void deleteRoom(UUID roomId);
}
