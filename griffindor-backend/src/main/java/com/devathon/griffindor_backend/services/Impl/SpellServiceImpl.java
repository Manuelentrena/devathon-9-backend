package com.devathon.griffindor_backend.services.Impl;

import com.devathon.griffindor_backend.dtos.DuelResultDto;
import com.devathon.griffindor_backend.models.Spell;
import com.devathon.griffindor_backend.repositories.SpellRepository;
import com.devathon.griffindor_backend.services.SpellService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SpellServiceImpl implements SpellService {

    @Autowired
    SpellRepository spellRepository;

    // Get all spells
    @Override
    public List<Spell> getAll() {
        return spellRepository.findAll();
    }

    @Override
    public DuelResultDto resolveDuel(UUID spellUser1, UUID spellUser2) {

        Spell spell1 = spellRepository.findById(spellUser1).orElseThrow(() -> new EntityNotFoundException("Spell not found: " + spellUser1));
        Spell spell2 = spellRepository.findById(spellUser2).orElseThrow(() -> new EntityNotFoundException("Spell not found: " + spellUser2));


        if (spell1.equals(spell2)) {
            return new DuelResultDto((Spell) null, null, true);
        }

        if (spell1.getCounterSpell() != null && spell1.getCounterSpell().equals(spell2)) {
            return new DuelResultDto(spell2, spell1, false);
        }

        return new DuelResultDto(spell1, spell2, false);
    }

    @Override
    public boolean spellExist(UUID spellId) {
        return spellRepository.existsById(spellId);
    }

}
