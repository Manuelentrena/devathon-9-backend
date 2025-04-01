package com.devathon.griffindor_backend.Queues;

import com.devathon.griffindor_backend.events.PlayersListEvent;
import org.springframework.stereotype.Component;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class PlayersListQueue {

    private final AtomicReference<PlayersListEvent> latestEvent = new AtomicReference<>();

    public void enqueue(PlayersListEvent event) {
        latestEvent.set(event);
    }

    public PlayersListEvent pollLatest() {
        return latestEvent.getAndSet(null);
    }
}
