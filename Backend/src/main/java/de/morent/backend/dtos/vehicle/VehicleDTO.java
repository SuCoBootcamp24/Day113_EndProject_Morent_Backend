package de.morent.backend.dtos.vehicle;

import de.morent.backend.entities.Image;
import de.morent.backend.enums.CarType;
import de.morent.backend.enums.FuelType;

public record VehicleDTO(
        long id,
        CarType carType,
        String brand,
        String model,
        int seats,
        int engineCapacity,
        boolean isAutomatic,
        FuelType fuelType,
        Image thumbnail,
        float consumption
) {
}
