package de.morent.backend.dtos.bookings;

import de.morent.backend.dtos.store.BookingStoreResponseDto;
import de.morent.backend.dtos.vehicle.VehicleDTO;
import de.morent.backend.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookingResponseDto(
        Long id,
        String bookingNumber,
        String bookingDate,
        String userFirstName,
        String userLastName,
        LocalDate dateOfBirth,
        VehicleDTO vehicle,
        LocalDate pickUpDate,
        LocalDate plannedDropOffDate,
        BookingStoreResponseDto pickUpLocation,
        BookingStoreResponseDto dropOffLocation,
        BigDecimal pricePerDay,
        int totalDays,
        BigDecimal totalPrice,
        boolean dropOffDifferentStoreExtraCharge,
        BookingStatus status,
        String imageUrl
) {
}
