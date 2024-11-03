package de.morent.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Newsletter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String firstName;

    String lastName;

    @Column(unique = true)
    String email;

    boolean isRegistered = false;
}
