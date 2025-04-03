
package com.devathon.griffindor_backend.Queues;

import com.devathon.griffindor_backend.events.RoomReadyEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class RoomReadyQueue {
    private final BlockingQueue<RoomReadyEvent> queue = new LinkedBlockingQueue<>();

    public void enqueue(RoomReadyEvent event) {
        queue.offer(event);
    }

    public RoomReadyEvent poll() {
        return queue.poll();
    }
}
