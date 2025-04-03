package com.devathon.griffindor_backend.dtos;

import com.devathon.griffindor_backend.models.Spell;
import lombok.Getter;

import java.util.UUID;

@Getter
public class SpellResponseDto {
        private UUID id;
        private String name;
        private SpellShortDto counterSpell;

        public SpellResponseDto(Spell spell) {
            this.id = spell.getId();
            this.name = spell.getName();
            this.counterSpell = (spell.getCounterSpell() != null) ? new SpellShortDto(spell.getCounterSpell()) : null;
        }
}
