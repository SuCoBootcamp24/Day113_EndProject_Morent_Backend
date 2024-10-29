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
        when(tokenService.generateToken(authentication)).thenReturn(expectedToken);

        String actualToken = authService.getToken(authentication);

        assertEquals(expectedToken, actualToken, "The token generated should match the expected token");
        verify(tokenService, times(1)).generateToken(authentication);
    }

    @Test
    public void testGetToken_NullAuthentication() {
        when(tokenService.generateToken(null)).thenReturn(null);

        String actualToken = authService.getToken(null);

        assertEquals(null, actualToken, "The token should be null if authentication is null");
        verify(tokenService, times(1)).generateToken(null);
    }
}