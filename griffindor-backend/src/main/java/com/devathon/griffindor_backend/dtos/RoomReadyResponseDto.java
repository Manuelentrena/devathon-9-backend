package com.devathon.griffindor_backend.dtos;

import java.util.UUID;

public record RoomReadyResponseDto(UUID room_id, PlayerDto oponent) {}