package de.morent.backend.configurations;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityConfigurationTest {

    @InjectMocks
    private SecurityConfiguration mockSecurityConfiguration;

    @Mock
    private RsaKeyProperties mockRsaKeys;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // key generation
        KeyPair keyPair = generateRsaKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        when(mockRsaKeys.publicKey()).thenReturn(publicKey);
        when(mockRsaKeys.privateKey()).thenReturn(privateKey);

        mockSecurityConfiguration = new SecurityConfiguration(mockRsaKeys);
    }

    // help method to generating RSA key pair
    private KeyPair generateRsaKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    @Test
    public void testCorsConfigurationSource() {
        CorsConfigurationSource corsSource = mockSecurityConfiguration.corsConfigurationSource();
        assertNotNull(corsSource, "CorsConfigurationSource should not be null");
    }

    @Test
    public void testJwtDecoder() {
        JwtDecoder jwtDecoder = mockSecurityConfiguration.jwtDecoder();
        assertNotNull(jwtDecoder, "JwtDecoder sollte nicht null sein");
    }

    @Test
    public void testJwtEncoder() {
        JwtEncoder jwtEncoder = mockSecurityConfiguration.jwtEncoder();
        assertNotNull(jwtEncoder, "JwtEncoder sollte nicht null sein");
    }
}