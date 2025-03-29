package com.devathon.griffindor_backend.dtos;

public class PlayerCountDto {
    private final int num_players;

    public PlayerCountDto(int num_players) {
        this.num_players = num_players;
    }

    public int getNum_players() {
        return num_players;
    }
}