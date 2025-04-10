package com.devathon.griffindor_backend.dtos;

import java.util.UUID;

public record PlayerSpellDto (String sessionId, UUID spellId) {
}
