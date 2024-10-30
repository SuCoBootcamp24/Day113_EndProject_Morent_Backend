package de.morent.backend.services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private TokenService tokenService;

    public AuthService(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public String getToken(Authentication auth, String firstname) {
        return tokenService.generateToken(auth, firstname);
    }
}
