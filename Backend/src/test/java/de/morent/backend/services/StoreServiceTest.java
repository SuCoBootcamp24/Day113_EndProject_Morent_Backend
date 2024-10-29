package de.morent.backend.services;

import de.morent.backend.dtos.store.StoreRequestDTO;
import de.morent.backend.entities.Store;
import de.morent.backend.entities.User;
import de.morent.backend.enums.UserRole;
import de.morent.backend.repositories.StoreRepository;
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
    private StoreRepository mockStoreRepository;

    @Mock
    private UserService mockUserService;

    @InjectMocks
    private StoreService storeService;

    private StoreRequestDTO storeRequestDTO;
    private User adminUser;
    private User managerUser;
    private User customerUser;

    @BeforeEach
    public void setup() {
        storeRequestDTO = new StoreRequestDTO(
                "StoreName",
                "Street",
                "123",
                "12345",
                "City",
                "Country",
                "Coordinates",
                1L
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
        when(mockStoreRepository.findByName(storeRequestDTO.name())).thenReturn(Optional.empty());
        when(mockUserService.findUserById(storeRequestDTO.managerId())).thenReturn(Optional.of(managerUser));

        boolean result = storeService.createNewStore(storeRequestDTO);

        assertTrue(result);
        verify(mockStoreRepository, times(1)).save(any(Store.class));
    }

    @Test
    public void testCreateNewStore_StoreAlreadyExists() {
        when(mockStoreRepository.findByName(storeRequestDTO.name())).thenReturn(Optional.of(new Store()));

        boolean result = storeService.createNewStore(storeRequestDTO);

        assertFalse(result);
        verify(mockStoreRepository, never()).save(any(Store.class));
    }

    @Test
    public void testCreateNewStore_UserNotFound() {
        when(mockStoreRepository.findByName(storeRequestDTO.name())).thenReturn(Optional.empty());
        when(mockUserService.findUserById(storeRequestDTO.managerId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> storeService.createNewStore(storeRequestDTO));
        verify(mockStoreRepository, never()).save(any(Store.class));
    }

    @Test
    public void testCreateNewStore_UserIsNotAdmin() {
        when(mockStoreRepository.findByName(storeRequestDTO.name())).thenReturn(Optional.empty());
        when(mockUserService.findUserById(storeRequestDTO.managerId())).thenReturn(Optional.of(customerUser));

        assertThrows(IllegalArgumentException.class, () -> storeService.createNewStore(storeRequestDTO));
        verify(mockStoreRepository, never()).save(any(Store.class));
    }

}