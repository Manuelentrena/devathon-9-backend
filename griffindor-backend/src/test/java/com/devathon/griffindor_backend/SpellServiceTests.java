package com.devathon.griffindor_backend;

import com.devathon.griffindor_backend.dtos.PlayerSpellDto;
import com.devathon.griffindor_backend.dtos.RoundRequestDto;
import com.devathon.griffindor_backend.dtos.RoundResponseDto;
import com.devathon.griffindor_backend.dtos.RoundResult;
import com.devathon.griffindor_backend.enums.RoomVisibility;
import com.devathon.griffindor_backend.enums.RoundStatus;
import com.devathon.griffindor_backend.models.Room;
import com.devathon.griffindor_backend.models.Spell;
import com.devathon.griffindor_backend.repositories.SpellRepository;
import com.devathon.griffindor_backend.services.Impl.SpellServiceImpl;
import com.devathon.griffindor_backend.services.PlayerService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class SpellServiceTests {
    @Mock
    private SpellRepository spellRepository; // SOLO es un simulación del repositorio

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private SpellServiceImpl spellService; // Servicio a probar

    private Spell expelliarmus, avadaKedavra, protego;
    private Room room;

    @BeforeEach // Ejecutar antes del test
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Crear hechizos
        expelliarmus = new Spell(UUID.randomUUID(), "Expelliarmus", null);
        avadaKedavra = new Spell(UUID.randomUUID(), "Avada Kedavra", expelliarmus);
        protego = new Spell(UUID.randomUUID(), "Protego", avadaKedavra);

        // Crear sala
        room = new Room(RoomVisibility.PUBLIC);
        room.addPlayer("sessionId1");
        room.addPlayer("sessionId2");

        // Asignar el contrahechizo de expelliarmus el cual es protego
        expelliarmus.setCounterSpell(protego);

        // Simular base de datos
        when(spellRepository.findById(expelliarmus.getId())).thenReturn(Optional.of(expelliarmus));
        when(spellRepository.findById(avadaKedavra.getId())).thenReturn(Optional.of(avadaKedavra));
        when(spellRepository.findById(protego.getId())).thenReturn(Optional.of(protego));

        // Simular jugadores
        when(playerService.getPlayerName("sessionId1")).thenReturn("Harry");
        when(playerService.getPlayerName("sessionId2")).thenReturn("Draco");

    }

    @Test
    void testDuel_ExpelliarmusWins() {
        room.getPlayers().get("sessionId1").addSpell(expelliarmus.getId());
        room.getPlayers().get("sessionId2").addSpell(avadaKedavra.getId());

        RoundResult result = spellService.resolveRound(room);

        assertEquals(RoundStatus.WINNER, result.status());
        assertEquals("sessionId1", result.winner());
    }

    @Test
    void testDuel_AvadaKadavraWins() {
        room.getPlayers().get("sessionId1").addSpell(avadaKedavra.getId());
        room.getPlayers().get("sessionId2").addSpell(protego.getId());

        RoundResult result = spellService.resolveRound(room);

        assertEquals(RoundStatus.WINNER, result.status());
        assertEquals("sessionId1", result.winner());
    }

    @Test
    void testDuel_isTie() {
        room.getPlayers().get("sessionId1").addSpell(expelliarmus.getId());
        room.getPlayers().get("sessionId2").addSpell(expelliarmus.getId());

        RoundResult result = spellService.resolveRound(room);

        assertEquals(RoundStatus.DRAW, result.status());
        assertNull(result.winner());
    }
}