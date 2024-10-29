package de.morent.backend.configurations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigurationTest {

    @InjectMocks
    private SecurityConfiguration mockSecurityConfiguration;

    @Mock
    private RsaKeyProperties mockRsaKeys;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCorsConfigurationSource() {
        CorsConfigurationSource corsConfigurationSource = mockSecurityConfiguration.corsConfigurationSource();
        assertNotNull(corsConfigurationSource);

        CorsConfiguration corsConfiguration = corsConfigurationSource.getCorsConfiguration(null);
        assertNotNull(corsConfiguration);

        assert(corsConfiguration.getAllowedOrigins().contains("*"));
        assert(corsConfiguration.getAllowedMethods().containsAll(List.of("GET", "POST", "PUT", "DELETE")));
        assert(corsConfiguration.getAllowedHeaders().containsAll(List.of("Authorization", "Content-type")));
    }

}