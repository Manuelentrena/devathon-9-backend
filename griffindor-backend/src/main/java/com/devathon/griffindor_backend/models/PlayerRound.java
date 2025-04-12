package com.devathon.griffindor_backend.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
public class PlayerRound {
    private final List<UUID> spells = new ArrayList<>();
    private int roundsWon = 0;

    public void addSpell(UUID spellId) {
        spells.add(spellId);
    }

    public UUID getSpellForRound(int roundNumber) {
        int index = roundNumber - 1; // Ronda 1 → índice 0
        if (index < 0 || index >= spells.size()) {
            throw new IllegalArgumentException("Invalid round: " + roundNumber);
        }
        return spells.get(index);
    }

    public void incrementRoundsWon() {
        roundsWon++;
    }

    public void resetSpells() {
        spells.clear();
    }
}
