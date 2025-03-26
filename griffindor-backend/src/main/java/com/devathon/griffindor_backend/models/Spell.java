package com.devathon.griffindor_backend.models;

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

    @ManyToOne
    @JoinColumn(name = "counter_spell_id")
    private Spell counterSpell;

}
