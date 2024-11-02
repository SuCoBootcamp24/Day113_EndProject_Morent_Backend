package de.morent.backend.dtos;

public record AddressDTO(
        String street,
        String houseNumber,
        String zipCode,
        String city,
        String country,
        String coordinates
) {
}
