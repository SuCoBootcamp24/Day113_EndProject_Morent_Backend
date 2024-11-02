package de.morent.backend.dtos.user;

import de.morent.backend.dtos.address.AddressDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserDetailsDTO(
        String email,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String phoneNumber,
        AddressDTO address,
        String profilePictureUrl,
        LocalDateTime createdDate,
        LocalDateTime updatedDate
) {
}
