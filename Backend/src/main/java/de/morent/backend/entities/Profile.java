package de.morent.backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.transaction.reactive.GenericReactiveTransaction;

import java.time.LocalDate;

@Entity
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = {CascadeType.ALL})
    private Address address;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String phoneNumber;

    @OneToOne(cascade = {CascadeType.ALL})
    private Image image;
}
