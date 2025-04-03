package com.devathon.griffindor_backend.dtos;

import com.devathon.griffindor_backend.models.Spell;
import lombok.Getter;

import java.util.UUID;

@Getter
public class SpellShortDto {
    private UUID id;
    private String name;

    public SpellShortDto(Spell spell) {
        this.id = spell.getId();
        this.name = spell.getName();
    }
}
