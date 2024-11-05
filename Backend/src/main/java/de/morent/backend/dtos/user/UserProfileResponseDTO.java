package de.morent.backend.dtos.user;

import de.morent.backend.dtos.address.AddressDTO;

import java.time.LocalDate;

public record UserProfileResponseDTO(

        String firstName,
        String lastName,
        LocalDate birthDate,
        String phoneNumber,
        String profilePictureUrl,
        AddressDTO address
) {
}
