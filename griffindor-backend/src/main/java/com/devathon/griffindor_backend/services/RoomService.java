package com.devathon.griffindor_backend.services;

import com.devathon.griffindor_backend.enums.RoomVisibility;
import com.devathon.griffindor_backend.models.Room;

import java.util.UUID;

public interface RoomService {

    /**
     * Crea una nueva sala y la devuelve.
     */
    Room createRoom(RoomVisibility visibility);

    /**
     * Añade un jugador a la sala.
     *
     * @param roomId   UUID de la sala
     * @param playerId ID del jugador
     * @return true si el jugador se añadió con éxito
     */
    boolean joinRoom(UUID roomId, String playerId);

    /**
     * Elimina un jugador de la sala.
     *
     * @param roomId   UUID de la sala
     * @param playerId ID del jugador
     * @return true si el jugador se eliminó con éxito
     */
    boolean takeOutRoom(UUID roomId, String playerId);

    /**
     * Indica si una sala está llena.
     *
     * @param roomId UUID de la sala
     * @return true si está llena
     */
    boolean isFull(UUID roomId);

    /**
     * Verifica si un jugador pertenece a una sala.
     *
     * @param roomId   UUID de la sala
     * @param playerId ID del jugador
     * @return true si el jugador pertenece a la sala
     */
    boolean belongsRoom(UUID roomId, String playerId);
}
