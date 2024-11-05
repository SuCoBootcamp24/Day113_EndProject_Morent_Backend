package de.morent.backend.dtos.address;

public record AddressDTO(
        String street,
        String houseNumber,
        String zipCode,
        String city,
        String country,
        String coordinates,
        boolean isRealUserAddress
) {
}
