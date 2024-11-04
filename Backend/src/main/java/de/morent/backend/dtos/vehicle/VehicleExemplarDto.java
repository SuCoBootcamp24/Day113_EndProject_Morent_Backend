package de.morent.backend.dtos.vehicle;

import de.morent.backend.dtos.bookings.DamageDto;
import de.morent.backend.entities.Damage;
import de.morent.backend.entities.Review;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record VehicleExemplarDto(
        long id,
        VehicleDTO vehicle,
        BigDecimal pricePerDay,
        int mileage,
        String status,
        LocalDate createAt,
        List<DamageDto> Damages,
        int ConstYear
) {
}
