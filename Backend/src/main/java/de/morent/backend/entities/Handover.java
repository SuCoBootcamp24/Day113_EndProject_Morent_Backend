package de.morent.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Handover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate date;

    @Column
    private int newMileage;

    @Column
    private boolean isTankFull;

    @OneToMany
    @JoinColumn(name = "damage_id")
    private List<Damage> damages;
}