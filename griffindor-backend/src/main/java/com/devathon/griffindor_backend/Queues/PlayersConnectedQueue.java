package com.devathon.griffindor_backend.Queues;

import com.devathon.griffindor_backend.events.PlayersConnectedEvent;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class PlayersConnectedQueue {

    private final AtomicReference<PlayersConnectedEvent> latestEvent = new AtomicReference<>();

    public void enqueue(PlayersConnectedEvent event) {
        latestEvent.set(event);
    }

    public PlayersConnectedEvent pollLatest() {
        return latestEvent.getAndSet(null);
    }
}
