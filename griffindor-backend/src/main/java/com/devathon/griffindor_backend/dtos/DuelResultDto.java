package com.devathon.griffindor_backend.dtos;

import com.devathon.griffindor_backend.models.Spell;
import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class DuelResultDto {
    private final Spell winner;
    private final Spell loser;
    private final boolean isTie;

}
