package de.morent.backend.entities;

import de.morent.backend.enums.DamagePosition;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Damage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @Enumerated(EnumType.STRING)
    private DamagePosition damagePosition;

    @Column
    private String damageDescription;

    @Column
    private LocalDate created;

    @Column
    private boolean isRepaired;

/*
    @ManyToOne
    private DamageProfile damageProfile;
*/


}
