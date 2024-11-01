package de.morent.backend.services;

import de.morent.backend.dtos.store.StoreRequestDTO;
import de.morent.backend.entities.Address;
import de.morent.backend.entities.Store;
import de.morent.backend.entities.User;
import de.morent.backend.enums.UserRole;
import de.morent.backend.repositories.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StoreService {

    private StoreRepository storeRepository;
    private UserService userService;

    public StoreService(StoreRepository storeRepository, UserService userService) {
        this.storeRepository = storeRepository;
        this.userService = userService;
    }

    @Transactional
    public boolean createNewStore(StoreRequestDTO dto) {
        Optional<Store> existingStore = storeRepository.findByName(dto.name());
        if (existingStore.isPresent()) return false;

        Store newStore = new Store();
        newStore.setName(dto.name());

        Address newAddress = new Address();
        newAddress.setStreet(dto.street());
        newAddress.setHouseNumber(dto.houseNumber());
        newAddress.setZipCode(dto.zipCode());
        newAddress.setCity(dto.city());
        newAddress.setCountry(dto.country());
        newAddress.setCoordinates(dto.coordinates());

        newStore.setAddress(newAddress);

        User newManager = userService.findUserById(dto.managerId()).orElseThrow(() -> new NoSuchElementException("User was not found"));
        if (!newManager.getRole().equals(UserRole.MANAGER)) throw new IllegalArgumentException("User is not a manager");
        newStore.setManager(newManager);

        storeRepository.save(newStore);
        return true;
    }
}
