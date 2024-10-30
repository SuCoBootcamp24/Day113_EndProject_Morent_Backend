package de.morent.backend.services;

import de.morent.backend.dtos.auth.AuthResponseDTO;
import de.morent.backend.dtos.auth.SignUpRequestDto;
import de.morent.backend.entities.Profile;

import de.morent.backend.entities.User;
import de.morent.backend.enums.UserRole;
import de.morent.backend.repositories.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private AuthService authService;
    private PasswordEncoder passwordEncoder;
    private VerifyService verifyService;
    private RedisService redisService;
    private TokenService tokenService;

    public UserService(UserRepository userRepository, AuthService authService, PasswordEncoder passwordEncoder, VerifyService verifyService, RedisService redisService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.verifyService = verifyService;
        this.redisService = redisService;
        this.tokenService = tokenService;
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

    public User newRegistrationUser(SignUpRequestDto dto) {
        User user = new User();
        Profile profile = new Profile();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        profile.setFirstName(dto.firstName());
        profile.setLastName(dto.lastName());
        profile.setUser(user);
        user.setRole(UserRole.USER);
        user.setProfile(profile);
        userRepository.save(user);
        verifyService.sendVerifyMail(user.getEmail());
        return user;
    }

    public AuthResponseDTO unlockAccount(String verifyCode) {
        String userEmail = redisService.getValue(verifyCode);
        if (userEmail.isEmpty()) {
            throw new IllegalStateException("Verification code invalid or expired");
        }
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new NoSuchElementException("User not found"));
        user.setAccountNonLocked(true);

        redisService.deleteValue(verifyCode);
        String token = tokenService.generateToken(getAuthentication(user), user.getProfile().getFirstName());

        userRepository.save(user);
        return new AuthResponseDTO(token);
    }

    public Authentication getAuthentication(User user) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        return authentication;
    }


}
