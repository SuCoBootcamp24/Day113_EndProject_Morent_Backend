package de.morent.backend.services;

import de.morent.backend.dtos.favorite.FavoriteDto;
import de.morent.backend.dtos.favorite.FavoritesResponseDto;
import de.morent.backend.entities.Favorite;
import de.morent.backend.entities.User;
import de.morent.backend.entities.Vehicle;
import de.morent.backend.repositories.FavoriteRepository;
import de.morent.backend.repositories.UserRepository;
import de.morent.backend.repositories.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;


    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public void setFavorite(Authentication authentication, long vehicleId){
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(()->new EntityNotFoundException("User not found"));
        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(()->new EntityNotFoundException("Vehicle not found"));

        System.out.println(vehicle);
       Optional<Favorite> existingFavorite = favoriteRepository.findByUserIdAndVehicleId(user.getId(), vehicleId);

       if(existingFavorite.isPresent()) {
           favoriteRepository.delete(existingFavorite.get());
       }else{
           Favorite favorite = new Favorite(user, vehicle);
        favoriteRepository.save(favorite);
        }
    }

    public FavoritesResponseDto getAllFavoritesFromUser(Authentication authentication){
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(()->new EntityNotFoundException("User not found"));
        List<Favorite> favoriteList = favoriteRepository.findAllByUserId(user.getId()).orElseThrow(()-> new NoSuchElementException("User has not a favorites"));
        List<FavoriteDto> favoriteDtoList = favoriteList.stream().map(favorite ->
                new FavoriteDto(favorite.getVehicle().getId())).toList();
        return new FavoritesResponseDto(user.getId(), favoriteDtoList);
    }

}
