package com.devathon.griffindor_backend.events;

import com.devathon.griffindor_backend.dtos.PlayerDto;
import com.devathon.griffindor_backend.dtos.RoomReadyResponseDto;
import com.devathon.griffindor_backend.models.Room;

public class RoomReadyEvent {

    private final Room room;
    private final String destination;

    public RoomReadyEvent(Room room, String destination) {
        this.room = room;
        this.destination = destination;
    }

    public Room getRoom() {
        return room;
    }

    public String getDestination() {
        return destination;
    }

    public RoomReadyResponseDto toDto(PlayerDto oponentDto) {
        return new RoomReadyResponseDto(room.getRoomId(), oponentDto);
    }
}
