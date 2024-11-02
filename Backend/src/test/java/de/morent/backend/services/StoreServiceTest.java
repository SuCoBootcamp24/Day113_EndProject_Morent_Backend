package de.morent.backend.services;

import de.morent.backend.dtos.AddressDTO;
import de.morent.backend.dtos.store.StoreRequestDTO;
import de.morent.backend.dtos.store.StoreShortDTO;
import de.morent.backend.entities.Address;
import de.morent.backend.entities.Store;
import de.morent.backend.entities.User;
import de.morent.backend.enums.UserRole;
import de.morent.backend.repositories.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private UserService userService;

    @Mock
    private GeocodingService geocodingService;

    @InjectMocks
    private StoreService storeService;

    private StoreRequestDTO storeRequestDTO;

    private Store store1;
    private User manager1;
    private Address address1;
    private Store store2;
    private Address address2;
    private User manager2;
    private StoreShortDTO storeShortDTO1;
    private StoreShortDTO storeShortDTO2;

    private User adminUser;
    private User managerUser;
    private User customerUser;

    @BeforeEach
    public void setup() {
        storeRequestDTO = new StoreRequestDTO(
                "store1",
                "Street1",
                "111",
                "11111",
                "SampleCity",
                "Country",
                "50.123, 10.456",
                1L
        );

        address1 = new Address();
        address1.setStreet("Street1");
        address1.setHouseNumber("111");
        address1.setZipCode("11111");
        address1.setCity("SampleCity");
        address1.setCountry("Country");
        address1.setCoordinates("51.2345, 10.1245");

        address2 = new Address();
        address2.setStreet("Street2");
        address2.setHouseNumber("222");
        address2.setZipCode("22222");
        address2.setCity("SampleCity");
        address2.setCountry("Country");
        address2.setCoordinates("10.1245, 51.2345,");

        manager1 = new User();
        manager1.setId(1L);
        manager1.setRole(UserRole.MANAGER);

        manager2 = new User();
        manager1.setId(2L);
        manager1.setRole(UserRole.MANAGER);

        store1 = new Store();
        store1.setId(1L);
        store1.setName("store1");
        store1.setAddress(address1);


        store2 = new Store();
        store2.setId(2L);
        store2.setName("store2");
        store2.setAddress(address2);


        storeShortDTO1 = new StoreShortDTO(
                store1.getId(),
               store1.getName(),
                new AddressDTO(
                        address1.getStreet(),
                        address1.getHouseNumber(),
                        address1.getZipCode(),
                        address1.getCity(),
                        address1.getCountry(),
                        address1.getCoordinates()
                ),
                0.0
        );

        storeShortDTO2 = new StoreShortDTO(
                store2.getId(),
                store2.getName(),
                new AddressDTO(
                        address2.getStreet(),
                        address2.getHouseNumber(),
                        address2.getZipCode(),
                        address2.getCity(),
                        address2.getCountry(),
                        address2.getCoordinates()
                ),
                0.0
        );

        // Admin User
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword("securePassword");
        adminUser.setRole(UserRole.ADMIN);
        adminUser.setAccountNonLocked(true);

        // Manager User
        managerUser = new User();
        managerUser.setId(2L);
        managerUser.setEmail("manager@example.com");
        managerUser.setPassword("userPassword");
        managerUser.setRole(UserRole.MANAGER);
        managerUser.setAccountNonLocked(true);

        // Customer User
        customerUser = new User();
        customerUser.setId(3L);
        customerUser.setEmail("user@example.com");
        customerUser.setPassword("userPassword");
        customerUser.setRole(UserRole.USER);
        customerUser.setAccountNonLocked(true);
    }


    @Test
    public void testCreateNewStore_SuccessfulCreation() {
        when(storeRepository.findByName(storeRequestDTO.name())).thenReturn(Optional.empty());
        when(userService.findUserById(storeRequestDTO.managerId())).thenReturn(Optional.of(managerUser));
        when(geocodingService.convertAddToCoords(any(Address.class))).thenReturn("50.123, 10.456");

        boolean result = storeService.createNewStore(storeRequestDTO);

        assertTrue(result, "The method should return true if the store is successfully created.");
        verify(storeRepository).save(any(Store.class));
    }


    @Test
    public void testCreateNewStore_StoreAlreadyExists() {
        when(storeRepository.findByName(storeRequestDTO.name())).thenReturn(Optional.of(store1));

        boolean result = storeService.createNewStore(storeRequestDTO);

        assertFalse(result, "The method should return false if the store with the given name already exists.");
        verify(storeRepository, never()).save(any(Store.class));
    }


    @Test
    public void testCreateNewStore_ManagerNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        when(storeRepository.findByName(storeRequestDTO.name())).thenReturn(Optional.empty());
        when(userService.findUserById(storeRequestDTO.managerId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> storeService.createNewStore(storeRequestDTO),
                "An EntityNotFoundException should be thrown if the manager is not found.");
    }


    @Test
    public void testCreateNewStore_UserIsNotManager_ThrowsIllegalArgumentException() {
        when(storeRepository.findByName(storeRequestDTO.name())).thenReturn(Optional.empty());
        when(userService.findUserById(storeRequestDTO.managerId())).thenReturn(Optional.of(customerUser));

        assertThrows(IllegalArgumentException.class, () -> storeService.createNewStore(storeRequestDTO),
                "An IllegalArgumentException should be thrown if the user is not a manager.");
    }


    //----------

}