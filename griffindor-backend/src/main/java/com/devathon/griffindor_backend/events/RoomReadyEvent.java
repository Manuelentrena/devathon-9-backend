package com.devathon.griffindor_backend.events;

import com.devathon.griffindor_backend.dtos.RoomIdResponseDto;
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

    public RoomIdResponseDto toDto() {
        return new RoomIdResponseDto(room.getRoomId());
    }
}
