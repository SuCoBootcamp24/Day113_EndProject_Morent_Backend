package de.morent.backend.dtos.search;

import de.morent.backend.enums.CarType;
import de.morent.backend.enums.FuelType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FilteringDto(
        long storeId,
        LocalDate startDate,
        LocalDate endDate,
        List<CarType> carType,
        List<FuelType> fuelType,
        BigDecimal pricePerDay
) {
}
