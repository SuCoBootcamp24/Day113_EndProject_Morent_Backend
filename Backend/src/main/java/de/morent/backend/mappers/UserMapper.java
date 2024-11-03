package de.morent.backend.mappers;

import de.morent.backend.dtos.user.UserDetailsDTO;
import de.morent.backend.dtos.user.UserProfileResponseDTO;
import de.morent.backend.entities.Profile;
import de.morent.backend.entities.User;

public class UserMapper {

    public static UserProfileResponseDTO toUserProfileResponseDTO(Profile userProfile) {
        return new UserProfileResponseDTO(
                userProfile.getFirstName(),
                userProfile.getLastName(),
                userProfile.getDateOfBirth(),
                userProfile.getPhoneNumber(),
                userProfile.getImage().getImageUrl(),
                AddressMapper.toDTO(userProfile.getAddress())
        );
    }

    public static UserDetailsDTO toUserDetailsDTO(User user) {
        return new UserDetailsDTO(
                user.getEmail(),
                user.getProfile().getFirstName(),
                user.getProfile().getLastName(),
                user.getProfile().getDateOfBirth(),
                user.getProfile().getPhoneNumber(),
                AddressMapper.toDTO(user.getProfile().getAddress()),
                user.getProfile().getImage().getImageUrl(),
                user.getCreated(),
                user.getUpdated()
        );
    }
}
