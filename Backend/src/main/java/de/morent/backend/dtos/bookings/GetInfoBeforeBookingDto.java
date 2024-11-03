package de.morent.backend.dtos.bookings;

import java.math.BigDecimal;
import java.time.LocalDate;

public record GetInfoBeforeBookingDto(
        String storeName,
        String storeCity,
        String dropOffStoreName,
        String dropOffStoreCity,
        LocalDate pickUpDate,
        LocalDate dropOffDate,
        long totalDays,
        BigDecimal pricePerDay,
        boolean hasExtraChargeChangingLocation,
        BigDecimal pauschale,
        BigDecimal totalPrice
) {
}
