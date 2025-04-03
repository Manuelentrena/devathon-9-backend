package com.devathon.griffindor_backend.Queues;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Component;

import com.devathon.griffindor_backend.enums.RoomVisibility;
import com.devathon.griffindor_backend.models.Room;

@Component
public class WaitlistQueue {

    private final Queue<Room> waitlist = new ConcurrentLinkedQueue<>();

    public Room pollAvailableRoom() {
        return waitlist.poll();
    }

    public void addRoom(Room room) {
        if (room.getVisibility() == RoomVisibility.PUBLIC && !room.isFull()) {
            waitlist.offer(room);
        }
    }

    public boolean isEmpty() {
        return waitlist.isEmpty();
    }

    public boolean removeRoom(Room room) {
        return waitlist.remove(room);
    }
}
