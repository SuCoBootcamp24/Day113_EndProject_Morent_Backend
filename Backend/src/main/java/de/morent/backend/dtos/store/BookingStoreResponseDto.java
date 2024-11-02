package de.morent.backend.dtos.store;

import de.morent.backend.dtos.address.AddressDTO;

public record BookingStoreResponseDto(
        String name,
        AddressDTO address
) {
}
