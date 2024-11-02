package de.morent.backend.dtos.user;

import de.morent.backend.dtos.address.AddressDTO;

import java.time.LocalDate;


public record UserProfileRequestDTO(
            String firstName,
            String lastName,
            LocalDate birthDate,
            String phoneNumber,
            String street,
            String houseNumber,
            String zipCode,
            String city,
            String country,
            String coordinates
) {
}
