package de.morent.backend.dtos.bookings;

import java.time.LocalDate;

public record BookingRequestDto(
        long vehicleExemplarId,
        long pickUpLocationId,
        LocalDate pickUpDate,
        long dropOffLocationId,
        LocalDate planedDropOffDate
) {
}
