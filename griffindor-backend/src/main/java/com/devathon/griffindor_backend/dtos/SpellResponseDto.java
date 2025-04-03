package com.devathon.griffindor_backend.dtos;

import com.devathon.griffindor_backend.models.Spell;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


import java.util.UUID;

@Schema(description = "Example payload model")
@Getter
public class SpellResponseDto {
        @Schema(description = "uuid", example = "axlkajzjxjajasdfasdf")
        private UUID id;
        @Schema(description = "name", example = "Fireball")
        private String name;
        private SpellShortDto counterSpell;

        public SpellResponseDto(Spell spell) {
            this.id = spell.getId();
            this.name = spell.getName();
            this.counterSpell = (spell.getCounterSpell() != null) ? new SpellShortDto(spell.getCounterSpell()) : null;
        }
}
