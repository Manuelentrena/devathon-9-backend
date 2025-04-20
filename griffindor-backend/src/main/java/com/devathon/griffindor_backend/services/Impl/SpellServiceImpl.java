package com.devathon.griffindor_backend.services.Impl;

import com.devathon.griffindor_backend.dtos.*;
import com.devathon.griffindor_backend.enums.RoundStatus;
import com.devathon.griffindor_backend.models.PlayerRound;
import com.devathon.griffindor_backend.models.Room;
import com.devathon.griffindor_backend.models.Spell;
import com.devathon.griffindor_backend.repositories.SpellRepository;
import com.devathon.griffindor_backend.services.SpellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public RoundResult resolveRound(Room room) {
        List<String> playerIds = new ArrayList<>(room.getPlayerIds());
        String player1Id = playerIds.get(0);
        String player2Id = playerIds.get(1);

        int round = room.getCurrentRound();

        UUID spell1Id = room.getPlayers().get(player1Id).getSpellForRound(round);
        UUID spell2Id = room.getPlayers().get(player2Id).getSpellForRound(round);

        if (spell1Id.equals(spell2Id)) {
            return new RoundResult(null, RoundStatus.DRAW);
        }

        Spell spell1 = spellRepository.findById(spell1Id)
                .orElseThrow(() -> new IllegalStateException("Spell not found: " + spell1Id));
        Spell spell2 = spellRepository.findById(spell2Id)
                .orElseThrow(() -> new IllegalStateException("Spell not found: " + spell2Id));

        Spell counterOfSpell1 = spell1.getCounterSpell();

        if (counterOfSpell1 != null && counterOfSpell1.getId().equals(spell2.getId())) {
            room.getPlayers().get(player2Id).incrementRoundsWon();
            return new RoundResult(player2Id, RoundStatus.WINNER);
        } else {
            room.getPlayers().get(player1Id).incrementRoundsWon();
            return new RoundResult(player1Id, RoundStatus.WINNER);
        }
    }

    @Override
    public boolean spellExist(UUID spellId) {
        return spellRepository.existsById(spellId);
    }

}
