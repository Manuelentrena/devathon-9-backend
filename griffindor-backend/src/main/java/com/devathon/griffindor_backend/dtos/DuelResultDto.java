package com.devathon.griffindor_backend.dtos;

import com.devathon.griffindor_backend.models.Spell;
import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
public class DuelResultDto {

    private final SpellShortDto winner;
    private final SpellShortDto loser;
    private final boolean isTie;

    public DuelResultDto(Spell winner, Spell loser, boolean isTie) {
        this.winner = winner != null ? new SpellShortDto(winner) : null;
        this.loser = loser != null ? new SpellShortDto(loser) : null;
        this.isTie = isTie;
    }
}
