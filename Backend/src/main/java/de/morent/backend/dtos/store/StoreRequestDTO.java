package de.morent.backend.dtos.store;

import jakarta.validation.constraints.NotBlank;

public record StoreRequestDTO(

        @NotBlank
        String name,
        @NotBlank
        String street,
        @NotBlank
        String houseNumber,
        @NotBlank
        String zipCode,
        @NotBlank
        String city,
        @NotBlank
        String country,
        long managerId

) {
}
