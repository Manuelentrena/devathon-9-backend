package com.devathon.griffindor_backend.services.Impl;

import com.devathon.griffindor_backend.enums.RoomVisibility;
import com.devathon.griffindor_backend.models.Room;
import com.devathon.griffindor_backend.services.RoomService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomServiceImpl implements RoomService {

    private final Map<UUID, Room> rooms = new ConcurrentHashMap<>();

    @Override
    public Room createRoom(RoomVisibility visibility) {
        Room room = new Room(visibility);
        rooms.put(room.getRoomId(), room);
        return room;
    }

    @Override
    public boolean joinRoom(UUID roomId, String playerId) {
        Room room = rooms.get(roomId);
        if (room == null)
            return false;
        return room.addPlayer(playerId);
    }

    @Override
    public boolean takeOutRoom(UUID roomId, String playerId) {
        Room room = rooms.get(roomId);
        if (room == null)
            return false;
        boolean removed = room.removePlayer(playerId);
        if (removed && room.isEmpty()) {
            rooms.remove(roomId);
        }
        return removed;
    }

    @Override
    public boolean isFull(UUID roomId) {
        Room room = rooms.get(roomId);
        return room != null && room.isFull();
    }

    @Override
    public boolean belongsRoom(UUID roomId, String playerId) {
        Room room = rooms.get(roomId);
        return room != null && room.containsPlayer(playerId);
    }
}
