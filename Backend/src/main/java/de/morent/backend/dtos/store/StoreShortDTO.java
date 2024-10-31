package de.morent.backend.dtos.store;

import de.morent.backend.dtos.AddressDTO;

public record StoreShortDTO(
        String name,
        AddressDTO address
) {
}
