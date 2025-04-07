package com.devathon.griffindor_backend.dtos;

import java.util.List;
import java.util.UUID;

public record CastSpellsDto (
        UUID roomId,
        List<UUID> spells
) {}
