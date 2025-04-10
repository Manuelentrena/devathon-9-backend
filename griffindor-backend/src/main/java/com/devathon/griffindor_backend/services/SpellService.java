package com.devathon.griffindor_backend.services;

import com.devathon.griffindor_backend.dtos.RoundResponseDto;
import com.devathon.griffindor_backend.dtos.RoundRequestDto;
import com.devathon.griffindor_backend.models.Room;
import com.devathon.griffindor_backend.models.Spell;

import java.util.List;
import java.util.UUID;

public interface SpellService {
    List<Spell> getAll();

    RoundResponseDto resolveRound(Room room, RoundRequestDto roundRequest);

    boolean spellExist(UUID spellId);
}
