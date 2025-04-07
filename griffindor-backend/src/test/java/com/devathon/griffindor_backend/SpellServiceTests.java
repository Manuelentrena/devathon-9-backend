package com.devathon.griffindor_backend;

import com.devathon.griffindor_backend.dtos.DuelResultDto;
import com.devathon.griffindor_backend.models.Spell;
import com.devathon.griffindor_backend.repositories.SpellRepository;
import com.devathon.griffindor_backend.services.Impl.SpellServiceImpl;
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

    @InjectMocks
    private SpellServiceImpl spellService; // Servicio a probar

    private Spell expelliarmus, avadaKedavra, protego;

    @BeforeEach // Ejecutar antes del test
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Crear hechizos
        expelliarmus = new Spell(UUID.randomUUID(), "Expelliarmus", null);
        avadaKedavra = new Spell(UUID.randomUUID(), "Avada Kedavra", expelliarmus);
        protego = new Spell(UUID.randomUUID(), "Protego", avadaKedavra);

        // Asignar el contrahechizo de expelliarmus el cual es protego
        expelliarmus.setCounterSpell(protego);

        // Simular base de datos
        when(spellRepository.findById(expelliarmus.getId())).thenReturn(Optional.of(expelliarmus));
        when(spellRepository.findById(avadaKedavra.getId())).thenReturn(Optional.of(avadaKedavra));
        when(spellRepository.findById(protego.getId())).thenReturn(Optional.of(protego));

    }

    @Test
    void testDuel_ExpelliarmusWins() {
        // Llama al méto.do resolveDuel de SpellService y pasa los ids de los hechizos
        DuelResultDto result = spellService.resolveDuel(expelliarmus.getId(), avadaKedavra.getId());

        //        Resultado esperado: (Spell winner, Spell loser, boolean isTie)           Resultado retornado
        assertEquals(new DuelResultDto(expelliarmus, avadaKedavra, false).toString(), result.toString());
    }

    @Test
    void testDuel_AvadaKadavraWins() {
        DuelResultDto result = spellService.resolveDuel(avadaKedavra.getId(), protego.getId());
        assertEquals(new DuelResultDto(avadaKedavra, protego, false).toString(), result.toString());
    }

    @Test
    void testDuel_isTie() {
        DuelResultDto result = spellService.resolveDuel(expelliarmus.getId(), expelliarmus.getId());
        assertEquals(new DuelResultDto((Spell) null, null, true).toString(), result.toString());
    }

    @Test
    void testDuel_spellNotFound_shouldThrowException() {
        UUID fakeId = UUID.randomUUID();
        when(spellRepository.findById(fakeId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            spellService.resolveDuel(fakeId, expelliarmus.getId());
        });

        assertTrue(exception.getMessage().contains("Spell not found"));
    }

}
