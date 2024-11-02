package de.morent.backend.dtos.bookings;

import java.util.List;

public record HandOverDto(
        long bookingId,
        int newMileage,
        boolean isTankFull,
        List<DamageDto> newDamages
) {
}
