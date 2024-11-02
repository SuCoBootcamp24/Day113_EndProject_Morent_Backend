package de.morent.backend.mappers;

import de.morent.backend.dtos.user.UserProfileResponseDTO;
import de.morent.backend.entities.Profile;

public class UserMapper {

    public static UserProfileResponseDTO toUserProfileResponseDTO(Profile userProfile) {
        return new UserProfileResponseDTO(
                userProfile.getFirstName(),
                userProfile.getLastName(),
                userProfile.getPhoneNumber(),
                userProfile.getImage().getImageUrl(),
                AddressMapper.toDTO(userProfile.getAddress())
        );
    }
}
