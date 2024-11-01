package de.morent.backend.services;

import de.morent.backend.converter.VowelConverter;
import de.morent.backend.dtos.store.StoreRequestDTO;
import de.morent.backend.dtos.store.StoreShortDTO;
import de.morent.backend.entities.Address;
import de.morent.backend.entities.Store;
import de.morent.backend.entities.User;
import de.morent.backend.enums.UserRole;
import de.morent.backend.mappers.AddressMapper;
import de.morent.backend.mappers.StoreMapper;
import de.morent.backend.repositories.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoreService {

    private StoreRepository storeRepository;
    private UserService userService;
    private GeocodingService geocodingService;

    private StoreMapper storeMapper;

    public StoreService(StoreRepository storeRepository, UserService userService, GeocodingService geocodingService, StoreMapper storeMapper) {
        this.storeRepository = storeRepository;
        this.userService = userService;
        this.geocodingService = geocodingService;
        this.storeMapper = storeMapper;
    }

    @Transactional
    public boolean createNewStore(StoreRequestDTO dto) {
        Optional<Store> existingStore = storeRepository.findByName(dto.name());
        if (existingStore.isPresent()) return false;

        Store newStore = new Store();
        newStore.setName(dto.name());

        Address newAddress = new Address();
        newAddress.setStreet(VowelConverter.convertString(dto.street()));
        newAddress.setHouseNumber(VowelConverter.convertString(dto.houseNumber()));
        newAddress.setZipCode(VowelConverter.convertString(dto.zipCode()));
        newAddress.setCity(VowelConverter.convertString(dto.city()));
        newAddress.setCountry(VowelConverter.convertString(dto.country()));
        newAddress.setCoordinates(geocodingService.convertAddToCoords(newAddress));

        newStore.setAddress(newAddress);

        User newManager = userService.findUserById(dto.managerId()).orElseThrow(() -> new NoSuchElementException("User was not found"));
        if (!newManager.getRole().equals(UserRole.MANAGER)) throw new IllegalArgumentException("User is not a manager");
        newStore.setManager(newManager);

        storeRepository.save(newStore);
        System.out.println(newStore);
        return true;
    }

    public List<StoreShortDTO> getStoresCloseByAddress(String city) {
        city = VowelConverter.convertString(city);
        List<Store> stores;
        stores = findStoreByCity(city);

        if (stores != null) return storeMapper.toListStoreShort(stores);
        return getFirstFiveStoresInRange(city);
    }

    private List<StoreShortDTO> getFirstFiveStoresInRange(String city) {
        List<Store> stores;
        stores = findAllStores();
        if (stores.isEmpty()) return List.of();
        return stores.stream()
                .map(store -> {
                    String searchLocation = geocodingService.getCoordinates(city);
                    double distance = geocodingService.calcDistance(
                            store.getName(),
                            store.getAddress().getCoordinates(),
                            city,
                            searchLocation
                            );

                    return new StoreShortDTO(
                            store.getId(),
                            store.getName(),
                            AddressMapper.toDTO(store.getAddress()),
                            distance
                            );
                })
                .collect(Collectors.toList());
    }

    private List<Store> findAllStores() {
        return storeRepository.findAll();
    }

    public List<StoreShortDTO> getAllStores() {
        return storeMapper.toListStoreShort(findAllStores());
    }

    private List<Store> findStoreByCity(String city) {
        List<Store> existingStore = storeRepository.findAllByAddress_City(city);
        return null;
    }

    public Optional<Store> findStoreById(long storeId) {
        return storeRepository.findById(storeId);
    }
}
