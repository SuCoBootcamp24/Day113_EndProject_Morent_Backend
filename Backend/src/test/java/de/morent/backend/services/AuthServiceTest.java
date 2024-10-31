package de.morent.backend.services;

import de.morent.backend.services.AuthService;
import de.morent.backend.services.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testGetToken_Success() {
        String expectedToken = "sampleJwtToken";
        String firstname = "John";

        when(tokenService.generateToken(authentication, firstname)).thenReturn(expectedToken);

        String actualToken = authService.getToken(authentication, firstname);

        assertEquals(expectedToken, actualToken, "The token generated should match the expected token");
        verify(tokenService, times(1)).generateToken(authentication, firstname);
    }

    @Test
    public void testGetToken_NullAuthentication() {
        String firstname = "John";

        when(tokenService.generateToken(null, firstname)).thenReturn(null);

        String actualToken = authService.getToken(null, firstname);

        assertEquals(null, actualToken, "The token should be null if authentication is null");
        verify(tokenService, times(1)).generateToken(null, firstname);
    }

    @Test
    public void testGetToken_NullFirstname() {
        String expectedToken = "sampleJwtToken";

        when(tokenService.generateToken(authentication, null)).thenReturn(expectedToken);

        String actualToken = authService.getToken(authentication, null);

        assertEquals(expectedToken, actualToken, "The token should be generated even if firstname is null");
        verify(tokenService, times(1)).generateToken(authentication, null);
    }
}