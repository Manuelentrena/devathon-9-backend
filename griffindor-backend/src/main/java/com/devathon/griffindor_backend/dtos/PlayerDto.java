package com.devathon.griffindor_backend.dtos;

import com.devathon.griffindor_backend.models.Player;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDto {
    private final String sessionId;
    private final String name;
    private final String house;
    private final String sessionStatus;

    public PlayerDto(Player player) {
        this.sessionId = player.getSessionId();
        this.name = player.getName();
        this.house = player.getHouse();
        this.sessionStatus = player.getSessionStatus().name();
    }

}