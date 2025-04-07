package com.devathon.griffindor_backend.Queues;

import com.devathon.griffindor_backend.events.DuelResultEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class DuelResultQueue {
    private final BlockingQueue<DuelResultEvent> queue = new LinkedBlockingQueue<>();

    public void enqueue(DuelResultEvent event) {
        queue.offer(event);
    }

    public DuelResultEvent poll() {
        return queue.poll();
    }
}
