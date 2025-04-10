package com.devathon.griffindor_backend.dtos;

public record RoundRequestDto(
        PlayerSpellDto player1,
        PlayerSpellDto player2
) {}
