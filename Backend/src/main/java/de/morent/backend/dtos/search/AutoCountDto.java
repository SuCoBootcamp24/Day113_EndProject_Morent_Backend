package de.morent.backend.dtos.search;

import java.util.List;

public record AutoCountDto(
        List<EnumDto> cartypes,
        List<EnumDto> fuelType

) {
}
