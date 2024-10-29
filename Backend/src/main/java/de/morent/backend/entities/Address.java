package de.morent.backend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
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
