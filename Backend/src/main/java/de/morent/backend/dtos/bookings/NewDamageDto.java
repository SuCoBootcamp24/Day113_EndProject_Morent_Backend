package de.morent.backend.dtos.bookings;

import de.morent.backend.enums.DamagePosition;

public record NewDamageDto(
        DamagePosition position,
        String description
) {
}
