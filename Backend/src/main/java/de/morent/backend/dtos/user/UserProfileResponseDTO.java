package de.morent.backend.dtos.user;

import de.morent.backend.dtos.address.AddressDTO;

public record UserProfileResponseDTO(

        String firstName,
        String lastName,
        String phoneNumber,
        String profilePictureUrl,
        AddressDTO address
) {
}
