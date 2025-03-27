package com.devathon.griffindor_backend.services.Impl;

import com.devathon.griffindor_backend.dtos.DuelResultDto;
import com.devathon.griffindor_backend.models.Spell;
import com.devathon.griffindor_backend.repositories.SpellRepository;
import com.devathon.griffindor_backend.services.SpellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SpellServiceImpl implements SpellService {

    @Autowired
    SpellRepository spellRepository;

    // Obtener hechizos
    @Override
    public List<Spell> getAll() {
        return spellRepository.findAll();
    }

    @Override
    public DuelResultDto resolveDuel(UUID spellUser1, UUID spellUser2) {
        // Obtiene los hechizos por su id. Si no se encuentran, lanza una excepción (orElseThrow)
        Spell spell1 = spellRepository.findById(spellUser1).orElseThrow();
        Spell spell2 = spellRepository.findById(spellUser2).orElseThrow();

        // Si son iguales, se declara empate
        if (spell1.equals(spell2)) {
            return new DuelResultDto(null, null, true);
        }

        // Si spell1 tiene un contra-hechizo y ese contra-hechizo es spell2, entonces spell2 gana el duelo
        if (spell1.getCounterSpell() != null && spell1.getCounterSpell().equals(spell2)) {
            return new DuelResultDto(spell2, spell1, false);
        }

        // Si ninguna de las condiciones anteriores se cumple, spell1 gana el duelo
        return new DuelResultDto(spell1, spell2, false);
    }

}
