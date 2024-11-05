package de.morent.backend.controller;

import de.morent.backend.dtos.favorite.FavoritesResponseDto;
import de.morent.backend.services.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorite")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<Void> setFavoriteVehicleFromUser(@RequestParam long vehicleId, Authentication authentication){
        favoriteService.setFavorite(authentication, vehicleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<FavoritesResponseDto> getAllFavorites(Authentication authentication){
        return ResponseEntity.ok(favoriteService.getAllFavoritesFromUser(authentication));
    }
}
