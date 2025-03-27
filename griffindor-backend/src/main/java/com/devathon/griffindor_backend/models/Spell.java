package com.devathon.griffindor_backend.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "counterSpell")
@Entity
public class Spell {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    //@Column(nullable = false, unique = true)
    //private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counter_spell_id")
    @JsonIgnoreProperties("counterSpell")
    private Spell counterSpell;

    public Spell(String name, Spell counterSpell) {
        this.name = name;
        this.counterSpell = counterSpell;
    }
}
