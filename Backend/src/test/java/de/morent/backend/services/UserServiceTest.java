package de.morent.backend.services;

import de.morent.backend.dtos.auth.AuthResponseDTO;
import de.morent.backend.entities.User;
import de.morent.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthService authService;
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
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(1L);

        assertTrue(result.isPresent(), "Expected user to be present");
        assertEquals(user, result.get(), "Expected user to match the mock user");
    }

    @Test
    public void testFindUserById_UserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = userService.findUserById(1L);

        assertTrue(result.isEmpty(), "Expected user to be absent");
    }

    @Test
    public void testGetUserByEmail_UserExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail("test@example.com");

        assertTrue(result.isPresent(), "Expected user to be present");
        assertEquals(user, result.get(), "Expected user to match the mock user");
    }

    @Test
    public void testGetUserByEmail_UserDoesNotExist() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByEmail("nonexistent@example.com");

        assertTrue(result.isEmpty(), "Expected user to be absent");
    }

    @Test
    public void testGetTokenByLogin_UserExists() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        String expectedToken = "mocked-token";
        when(authService.getToken(auth)).thenReturn(expectedToken);

        AuthResponseDTO result = userService.getTokenByLogin(auth);

        assertNotNull(result, "Expected AuthResponseDTO to be non-null");
        assertEquals(expectedToken, result.token(), "Expected token to match the mocked token");
    }

    @Test
    public void testGetTokenByLogin_UserDoesNotExist() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("nonexistent@example.com");
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.getTokenByLogin(auth);
        });
        assertEquals("User nonexistent@example.com not found", exception.getMessage(), "Expected error message to match");
    }
}