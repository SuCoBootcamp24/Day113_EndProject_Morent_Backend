package de.morent.backend.services;

import de.morent.backend.entities.User;
import de.morent.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
    }

    @Test
    public void testFindUserById_UserExists() {
        // Mock behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Call method
        Optional<User> result = userService.findUserById(1L);

        // Assertions
        assertTrue(result.isPresent(), "Expected user to be present");
        assertEquals(user, result.get(), "Expected user to match the mock user");
    }

    @Test
    public void testFindUserById_UserDoesNotExist() {
        // Mock behavior
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Call method
        Optional<User> result = userService.findUserById(1L);

        // Assertions
        assertTrue(result.isEmpty(), "Expected user to be absent");
    }
}