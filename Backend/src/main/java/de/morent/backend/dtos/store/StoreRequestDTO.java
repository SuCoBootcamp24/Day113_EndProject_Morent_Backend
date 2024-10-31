package de.morent.backend.dtos.store;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
        String coordinates,
        @NotNull
        long managerId

) {
}
