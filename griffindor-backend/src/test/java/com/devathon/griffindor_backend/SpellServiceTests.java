package com.devathon.griffindor_backend;

import com.devathon.griffindor_backend.dtos.PlayerSpellDto;
import com.devathon.griffindor_backend.dtos.RoundRequestDto;
import com.devathon.griffindor_backend.dtos.RoundResponseDto;
import com.devathon.griffindor_backend.enums.RoomVisibility;
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

    private RoundRequestDto createRoundRequest(UUID id1, UUID id2) {
        return new RoundRequestDto(
                new PlayerSpellDto("sessionId1", id1),
                new PlayerSpellDto("sessionId2", id2)
        );
    }


    @Test
    void testDuel_ExpelliarmusWins() {
        RoundRequestDto request = createRoundRequest(expelliarmus.getId(), avadaKedavra.getId());

        RoundResponseDto result = spellService.resolveRound(room, request);

        assertFalse(result.isTie());
        assertEquals("sessionId1", result.winner().sessionId());
        assertEquals("Expelliarmus", result.winner().spell().getName());
        assertEquals("Avada Kedavra", result.loser().spell().getName());
    }


    @Test
    void testDuel_AvadaKadavraWins() {
        RoundRequestDto request = createRoundRequest(avadaKedavra.getId(), protego.getId());

        RoundResponseDto result = spellService.resolveRound(room, request);

        assertFalse(result.isTie());
        assertEquals("sessionId1", result.winner().sessionId());
        assertEquals("Avada Kedavra", result.winner().spell().getName());
    }


    @Test
    void testDuel_isTie() {
        RoundRequestDto request = createRoundRequest(expelliarmus.getId(), expelliarmus.getId());

        RoundResponseDto result = spellService.resolveRound(room, request);

        assertTrue(result.isTie());
        assertNull(result.winner());
        assertNull(result.loser());
    }


    @Test
    void testDuel_spellNotFound_shouldThrowException() {
        UUID fakeId = UUID.randomUUID();
        when(spellRepository.findById(fakeId)).thenReturn(Optional.empty());

        RoundRequestDto request = createRoundRequest(fakeId, expelliarmus.getId());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            spellService.resolveRound(room, request);
        });

        assertTrue(exception.getMessage().contains("Spell not found"));
    }


}
