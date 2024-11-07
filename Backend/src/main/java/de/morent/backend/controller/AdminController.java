package de.morent.backend.controller;

import de.morent.backend.entities.*;
import de.morent.backend.enums.UserRole;
import de.morent.backend.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private AddressRepository addressRepository;
    private BookingRepository bookingRepository;
    private FavoriteRepository favoriteRepository;
    private HandoverRepository handoverRepository;
    private ImageRepository imageRepository;
    private NewsRepository newsRepository;
    private ProfileRepository profileRepository;
    private StoreRepository storeRepository;
    private UserRepository userRepository;
    private VehicleRepository vehicleRepository;
    private VehicleExemplarRepository vehicleExemplarRepository;

    public AdminController(AddressRepository addressRepository, BookingRepository bookingRepository, FavoriteRepository favoriteRepository, HandoverRepository handoverRepository, ImageRepository imageRepository, NewsRepository newsRepository, ProfileRepository profileRepository, StoreRepository storeRepository, UserRepository userRepository, VehicleRepository vehicleRepository, VehicleExemplarRepository vehicleExemplarRepository) {
        this.addressRepository = addressRepository;
        this.bookingRepository = bookingRepository;
        this.favoriteRepository = favoriteRepository;
        this.handoverRepository = handoverRepository;
        this.imageRepository = imageRepository;
        this.newsRepository = newsRepository;
        this.profileRepository = profileRepository;
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
        this.vehicleExemplarRepository = vehicleExemplarRepository;
    }

    @GetMapping("/address")
    public Iterable<Address> getAllAddresses() {
        return addressRepository.findAll();
    }


    @GetMapping("/bookings")
    public Iterable<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }


    @GetMapping("/favorites")
    public Iterable<Favorite> getAllFavorites() {
        return favoriteRepository.findAll();
    }

    @GetMapping("/handovers")
    public Iterable<Handover> getAllHandovers() {
        return handoverRepository.findAll();
    }


    @GetMapping("/images")
    public Iterable<Image> getAllImages() {
        return imageRepository.findAll();
    }





    @GetMapping("/news")
    public Iterable<Newsletter> getAllNews() {
        return newsRepository.findAll();
    }


    @GetMapping("/users")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/users")
    public void setUserRole(@RequestParam long userId, @RequestParam String role) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setRole(UserRole.valueOf(role));
        userRepository.save(user);
    }

    @GetMapping("/profiles")
    public Iterable<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @GetMapping("/stores")
    public Iterable<Store> getAllStores() {
        return storeRepository.findAll();
    }


    @GetMapping("/vehicles")
    public Iterable<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }
    @DeleteMapping("/vehicles")
    public void deleteAllVehicles() {
        vehicleRepository.deleteAll();
    }

    @GetMapping("/vehicle-exemplars")
    public Iterable<VehicleExemplar> getAllVehicleExemplars() {
        return vehicleExemplarRepository.findAll();
    }

    @DeleteMapping("/vehicle-exemplars")
    public void deleteAllVehicleExemplars() {
        vehicleExemplarRepository.deleteAll();
    }


}
