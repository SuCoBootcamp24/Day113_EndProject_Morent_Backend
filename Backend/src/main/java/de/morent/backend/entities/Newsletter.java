package de.morent.backend.entities;

import jakarta.persistence.*;

@Entity
public class Newsletter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String firsnName;

    String lastName;

    String email;

    boolean isRegistered = false;
}
