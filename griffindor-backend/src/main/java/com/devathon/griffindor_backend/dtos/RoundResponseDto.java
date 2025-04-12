package com.devathon.griffindor_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
public class RoundResponseDto {
    private int round;
    private boolean gameOver;
    private RoundResult result;
    Set<PlayerSpellDto> players = new HashSet<>();
}
