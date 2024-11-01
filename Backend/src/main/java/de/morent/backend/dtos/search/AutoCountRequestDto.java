package de.morent.backend.dtos.search;

import java.time.LocalDate;

public record AutoCountRequestDto(
        long storeId,
        LocalDate startDate,
        LocalDate endDate
) {
}
