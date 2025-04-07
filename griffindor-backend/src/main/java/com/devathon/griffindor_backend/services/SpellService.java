package com.devathon.griffindor_backend.services;

import com.devathon.griffindor_backend.dtos.DuelResultDto;
import com.devathon.griffindor_backend.models.Spell;

import java.util.List;
import java.util.UUID;

public interface SpellService {
    List<Spell> getAll();

    DuelResultDto resolveDuel(UUID spellId1, UUID spellId2);

    boolean spellExist(UUID spellId);
}
