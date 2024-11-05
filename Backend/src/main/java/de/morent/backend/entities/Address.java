package de.morent.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String street;

    @NotBlank
    private String houseNumber;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String city;

    @NotBlank
    private String country;

    private String coordinates;

    private boolean isRealUserAddress = false;



}
