package de.morent.backend.services;

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

        Optional<Favorite> favoriteList = favoriteRepository.findById(user.getId());

        Favorite favorite;
        if(favoriteList.isEmpty()){
            favorite = new Favorite();
            favorite.setUser(user);
            favorite.setVehicle(new ArrayList<>());
        }else{
            favorite = favoriteList.get();
        }

        if(!favorite.getVehicle().contains(vehicle)){
            favorite.getVehicle().add(vehicle);
        }else{
            favorite.getVehicle().remove(vehicle);
        }
        favoriteRepository.save(favorite);
    }
//
//    public FavoritesResponseDto getAllFavoritesFromUser(Authentication authentication){
//
//    }
}
