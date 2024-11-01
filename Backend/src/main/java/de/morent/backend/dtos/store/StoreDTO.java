package de.morent.backend.dtos.store;

import de.morent.backend.dtos.AddressDTO;

public record StoreDTO(
        long storeId,
        String name,
        AddressDTO address,
        int vehicleSize,
        int bookings,
        String managerName
) {
}
