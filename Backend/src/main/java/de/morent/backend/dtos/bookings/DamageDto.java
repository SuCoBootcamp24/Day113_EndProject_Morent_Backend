package de.morent.backend.dtos.bookings;

import de.morent.backend.enums.DamagePosition;

import java.time.LocalDate;

public record DamageDto(
        DamagePosition position,
        String description,
        LocalDate createdAt,
        boolean isRepaired
) {
}
