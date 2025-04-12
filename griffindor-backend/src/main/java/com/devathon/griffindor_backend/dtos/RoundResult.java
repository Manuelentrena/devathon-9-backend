package com.devathon.griffindor_backend.dtos;

import com.devathon.griffindor_backend.enums.RoundStatus;

public record RoundResult (String winner, RoundStatus status) {
}
