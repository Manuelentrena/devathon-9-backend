package com.devathon.griffindor_backend.services.Impl;

import com.devathon.griffindor_backend.dtos.RoundParticipantResultDto;
import com.devathon.griffindor_backend.dtos.RoundResponseDto;
import com.devathon.griffindor_backend.dtos.RoundRequestDto;
import com.devathon.griffindor_backend.dtos.SpellShortDto;
import com.devathon.griffindor_backend.models.Room;
import com.devathon.griffindor_backend.models.Spell;
import com.devathon.griffindor_backend.repositories.SpellRepository;
import com.devathon.griffindor_backend.services.PlayerService;
import com.devathon.griffindor_backend.services.SpellService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SpellServiceImpl implements SpellService {

    @Autowired
    SpellRepository spellRepository;

    @Autowired
    PlayerService playerService;

    // Get all spells
    @Override
    public List<Spell> getAll() {
        return spellRepository.findAll();
    }

    @Override
    public RoundResponseDto resolveRound(Room room, RoundRequestDto roundRequest) {

        Spell spell1 = spellRepository.findById(roundRequest.player1().spellId()).orElseThrow(() -> new EntityNotFoundException("Spell not found"));
        Spell spell2 = spellRepository.findById(roundRequest.player2().spellId()).orElseThrow(() -> new EntityNotFoundException("Spell not found"));

        if (spell1.equals(spell2)) {
            return new RoundResponseDto(true, null, null);
        }

        String player1Name = playerService.getPlayerName(roundRequest.player1().sessionId());
        String player2Name = playerService.getPlayerName(roundRequest.player2().sessionId());

        RoundParticipantResultDto p1 = new RoundParticipantResultDto(roundRequest.player1().sessionId(), player1Name, new SpellShortDto(spell1));
        RoundParticipantResultDto p2 = new RoundParticipantResultDto(roundRequest.player2().sessionId(), player2Name, new SpellShortDto(spell2));

        RoundResponseDto result;

        if (spell1.getCounterSpell() != null && spell1.getCounterSpell().equals(spell2)) {
            addWinnerToTheHistory(room, p2.sessionId());
            return new RoundResponseDto(false, p2, p1);
        }

        addWinnerToTheHistory(room, p1.sessionId());
        return new RoundResponseDto(false, p1, p2);
    }

    private void addWinnerToTheHistory(Room room, String playerId) {
        room.addRoundWinner(playerId);
    }

    @Override
    public boolean spellExist(UUID spellId) {
        return spellRepository.existsById(spellId);
    }

}
