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

        PlayerRound player1Round = room.getPlayers().get(player1Id);
        PlayerRound player2Round = room.getPlayers().get(player2Id);

        int round = room.getCurrentRound();

        UUID spell1Id = player1Round.getSpellForRound(round);
        UUID spell2Id = player2Round.getSpellForRound(round);

        RoundResult result;
        if (spell1Id.equals(spell2Id)) {
            return new RoundResult(null, RoundStatus.DRAW);
        }

        Optional<Spell> spell1 = spellRepository.findById(spell1Id);
        Optional<Spell> spell2 = spellRepository.findById(spell2Id);

        if (spell1.get().getCounterSpell() != null && spell1.get().getCounterSpell().equals(spell2)) {
            player1Round.incrementRoundsWon();
            return new RoundResult(player1Id, RoundStatus.WINNER);
        } else {
            player2Round.incrementRoundsWon();
            return new RoundResult(player2Id, RoundStatus.WINNER);
        }
    }

    @Override
    public boolean spellExist(UUID spellId) {
        return spellRepository.existsById(spellId);
    }

}
