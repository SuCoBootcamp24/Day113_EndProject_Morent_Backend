package de.morent.backend.dtos.bookings;

import java.util.List;

public record HandOverConfirmationDto(
        BookingShortResponseDto bookingData,
        int newMileage,
        boolean isTankFull,
        List<DamageDto> newDamages
) {
}
