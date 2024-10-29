package de.morent.backend.repositories;

import de.morent.backend.entities.Profile;
import jakarta.persistence.*;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String street;

    @Column
    private String houseNumber;

    @Column
    private String zipCode;

    @Column
    private String city;

    @Column
    private String country;

    @Column
    private String coordinates;

    @OneToOne
    private Profile profile;
}
