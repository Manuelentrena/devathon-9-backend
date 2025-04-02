package com.devathon.griffindor_backend.controllers;

import com.devathon.griffindor_backend.dtos.SpellResponseDto;
import com.devathon.griffindor_backend.models.Spell;
import com.devathon.griffindor_backend.services.SpellService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("spells")
public class SpellController {

    private final SpellService spellService;

    public SpellController(SpellService spellService) {
        this.spellService = spellService;
    }

    @GetMapping("")
    public ResponseEntity<List<SpellResponseDto>> getAllSpell(){
        // Entities to dtos
        List<SpellResponseDto> spells = spellService.getAll().stream()
                .map(SpellResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(spells);
    }
}
