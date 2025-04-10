package com.devathon.griffindor_backend.dtos;

public record RoundResponseDto(boolean isTie, RoundParticipantResultDto winner, RoundParticipantResultDto loser) {
}
