package de.morent.backend.dtos.bookings;

import de.morent.backend.dtos.store.BookingStoreResponseDto;
import de.morent.backend.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record BookingShortResponseDto(
        String bookingNumber,
        String userFirstName,
        String userLastName,
        String storeName,
        String storeCity,
        String dropOffStoreName,
        String dropOffStoreCity,
        LocalDate pickUpDate,
        LocalDate dropOffDate,
        BookingStatus status,
        BigDecimal totalPrice
) {
}
