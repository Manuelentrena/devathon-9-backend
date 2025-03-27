package com.devathon.griffindor_backend.repositories;

import com.devathon.griffindor_backend.models.Spell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpellRepository extends JpaRepository<Spell, UUID> {
    Optional<Spell> findByName(String name);
}
