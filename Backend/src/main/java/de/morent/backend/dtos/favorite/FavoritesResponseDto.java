package de.morent.backend.dtos.favorite;

import de.morent.backend.entities.Vehicle;

import java.util.List;

public record FavoritesResponseDto(
        long userId,
        List<FavoriteDto> vehicleList
) {
}
