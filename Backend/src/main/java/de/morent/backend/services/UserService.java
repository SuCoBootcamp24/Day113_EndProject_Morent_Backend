package de.morent.backend.services;

import de.morent.backend.dtos.auth.AuthResponseDTO;
import de.morent.backend.entities.User;
import de.morent.backend.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private AuthService authService;

    public UserService(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public Optional<User> findUserById(long userId) {
        return userRepository.findById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public AuthResponseDTO getTokenByLogin(Authentication auth) {
        Optional<User> existingUser = getUserByEmail(auth.getName());
        if (existingUser.isEmpty()) throw new UsernameNotFoundException("User " + auth.getName() + " not found");
        String token = authService.getToken(auth, existingUser.get().getProfile().getFirstName());
        return new AuthResponseDTO(token);
    }


}
