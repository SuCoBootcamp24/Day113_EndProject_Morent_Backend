package de.morent.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Vehicle vehicle;

    @Column
    private int stars;

    @Column
    private String description;

    @ManyToOne
    private User user;

    @Column
    private LocalDate created;

}
