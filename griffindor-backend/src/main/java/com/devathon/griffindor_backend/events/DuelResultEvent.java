package com.devathon.griffindor_backend.events;

import com.devathon.griffindor_backend.dtos.RoundResponseDto;
import lombok.Getter;

import java.util.UUID;

@Getter
public class DuelResultEvent {

    private final UUID roomId;
    private final RoundResponseDto duelResult;
    private final String destination;

    public DuelResultEvent(UUID roomId, RoundResponseDto duelResult, String destination) {
        this.roomId = roomId;
        this.duelResult = duelResult;
        this.destination = destination;
    }

}
