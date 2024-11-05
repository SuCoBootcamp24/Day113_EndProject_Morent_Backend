package de.morent.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Entity
@Data
public class Newsletter {

    @Id
    @Email
    String email;

    String firstName;

    String lastName;

    boolean isRegistered = false;
}
