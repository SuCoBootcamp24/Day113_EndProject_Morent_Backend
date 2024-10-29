package de.morent.backend.configurations;

import de.morent.backend.entities.User;
import de.morent.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AppConfigurationTest {

    @InjectMocks
    private AppConfiguration mockAppConfiguration;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockAppConfiguration = new AppConfiguration(mockUserRepository);
    }

    @Test
    public void testUserDetailsService_UserFound() {
        String username = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(username);
        mockUser.setPassword("password");

        when(mockUserRepository.findByEmail(username)).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = mockAppConfiguration.userDetailsService().loadUserByUsername(username);

        assertNotNull(userDetails, "UserDetails should not be null");
        assertEquals(username, userDetails.getUsername(), "username should be correct");
        assertEquals("password", userDetails.getPassword(), "Password should be correct");
    }

    @Test
    public void testUserDetailsService_UserNotFound() {
        String username = "test@example.com";
        when(mockUserRepository.findByEmail(username)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            mockAppConfiguration.userDetailsService().loadUserByUsername(username);
        }, "UsernameNotFoundException should be thrown when user not found");
    }

    @Test
    public void testPasswordEncoder() {
        BCryptPasswordEncoder passwordEncoder = mockAppConfiguration.passwordEncoder();
        assertNotNull(passwordEncoder, "BCryptPasswordEncoder should not be null");

        String rawPassword = "RealPassword123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword, "The encoded password should not be null");
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword), "The password should be correctly encrypted");
    }


}