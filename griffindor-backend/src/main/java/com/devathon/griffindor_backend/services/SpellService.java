package com.devathon.griffindor_backend.services;

import com.devathon.griffindor_backend.dtos.RoundResult;
import com.devathon.griffindor_backend.models.Room;
import com.devathon.griffindor_backend.models.Spell;

import java.util.List;
import java.util.UUID;

public interface SpellService {
    List<Spell> getAll();

    RoundResult resolveRound(Room room);

    boolean spellExist(UUID spellId);
}
