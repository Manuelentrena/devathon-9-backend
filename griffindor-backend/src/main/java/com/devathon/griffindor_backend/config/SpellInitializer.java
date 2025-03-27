package com.devathon.griffindor_backend.config;

import com.devathon.griffindor_backend.models.Spell;
import com.devathon.griffindor_backend.repositories.SpellRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Clase que inicializa los hechizos en la base de datos al iniciar la aplicación.
 * Verifica si ciertos hechizos existen en la base de datos y, si no, los crea y les asigna contra-hechizo.
 * */
@Component
public class SpellInitializer implements CommandLineRunner {
    private final SpellRepository spellRepository;

    public SpellInitializer(SpellRepository spellRepository) {
        this.spellRepository = spellRepository;
    }

    @Override
    public void run(String... args) {
        // Verifico si los hechizos ya existen en la bd y los creo si no existen
        if (spellRepository.findByName("Expelliarmus").isEmpty()) {
            spellRepository.save(new Spell("Expelliarmus", null));
        }
        if (spellRepository.findByName("Avada Kedavra").isEmpty()) {
            spellRepository.save(new Spell("Avada Kedavra", null));
        }
        if (spellRepository.findByName("Protego").isEmpty()) {
            spellRepository.save(new Spell("Protego", null));
        }

        // Recupero los hechizos para luego asignarle los contra-hechizos
        Spell expelliarmus = spellRepository.findByName("Expelliarmus").orElseThrow();
        Spell avadaKedavra = spellRepository.findByName("Avada Kedavra").orElseThrow();
        Spell protego = spellRepository.findByName("Protego").orElseThrow();

        // Asignar los contra-hechizos si todavía no están definidos
        if (expelliarmus.getCounterSpell() == null) expelliarmus.setCounterSpell(protego);
        if (avadaKedavra.getCounterSpell() == null) avadaKedavra.setCounterSpell(expelliarmus);
        if (protego.getCounterSpell() == null) protego.setCounterSpell(avadaKedavra);

        // Guardar los hechizos con los contra-hechizos asignados
        spellRepository.saveAll(List.of(expelliarmus, avadaKedavra, protego));
    }

}
