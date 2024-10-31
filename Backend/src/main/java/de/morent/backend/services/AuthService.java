package de.morent.backend.services;

import de.morent.backend.entities.User;
import de.morent.backend.repositories.UserRepository;
import org.springframework.expression.spel.ast.OpInc;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

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
