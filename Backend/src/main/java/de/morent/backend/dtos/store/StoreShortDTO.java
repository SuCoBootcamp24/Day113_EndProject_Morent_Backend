package de.morent.backend.dtos.store;

import de.morent.backend.dtos.AddressDTO;

public record StoreShortDTO(

        long storeId,
        String name,
        AddressDTO address,
        double distance
) {
}
