package de.morent.backend.services;

import de.morent.backend.dtos.auth.AuthResponseDTO;
import de.morent.backend.dtos.auth.SignUpRequestDto;
import de.morent.backend.dtos.user.UserProfileRequestDTO;
import de.morent.backend.dtos.user.UserProfileResponseDTO;
import de.morent.backend.entities.Address;
import de.morent.backend.entities.Profile;

import de.morent.backend.entities.User;
import de.morent.backend.enums.UserRole;
import de.morent.backend.mappers.UserMapper;
import de.morent.backend.repositories.AddressRepository;
import de.morent.backend.repositories.ProfileRepository;
import de.morent.backend.repositories.UserRepository;
import de.morent.backend.tools.PasswordGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    private UserRepository userRepository;
    private ProfileRepository profileRepository;
    private AddressRepository addressRepository;
    private AuthService authService;
    private PasswordEncoder passwordEncoder;
    private VerifyService verifyService;
    private RedisService redisService;
    private TokenService tokenService;

    public UserService(UserRepository userRepository, ProfileRepository profileRepository, AddressRepository addressRepository, AuthService authService, PasswordEncoder passwordEncoder, VerifyService verifyService, RedisService redisService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.addressRepository = addressRepository;
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

    @Transactional
    public void newRegistrationUser(SignUpRequestDto dto) {
        User user = new User();
        Profile profile = new Profile();
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        profile.setFirstName(dto.firstName());
        profile.setLastName(dto.lastName());
        user.setRole(UserRole.USER);
        user.setProfile(profile);
        userRepository.save(user);
        verifyService.sendVerifyMail(user.getEmail());
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


    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not Found"));
    }


    @Transactional
    public UserProfileResponseDTO updateUserProfile(UserProfileRequestDTO dto, Authentication auth) {
        System.out.println("JEEP");
        User user = findUserByEmail(auth.getName());
        if (!user.isAccountNonLocked()) throw new EntityNotFoundException("User was Deleted");

        Profile userProfile = user.getProfile();
        userProfile.setFirstName(dto.firstName());
        userProfile.setLastName(dto.lastName());
        userProfile.setDateOfBirth(dto.birthDate());
        userProfile.setPhoneNumber(dto.phoneNumber());

        Address userAddress = userProfile.getAddress() != null ? userProfile.getAddress() : new Address();
        userAddress.setStreet(dto.street());
        userAddress.setHouseNumber(dto.houseNumber());
        userAddress.setZipCode(dto.zipCode());
        userAddress.setCity(dto.city());
        userAddress.setCountry(dto.country());
        userAddress = addressRepository.save(userAddress);

        userProfile.setAddress(userAddress);
        userProfile = profileRepository.save(userProfile);

        return UserMapper.toUserProfileResponseDTO(userProfile);
    }

    public boolean deleteUser(Authentication auth) {
        User user = findUserByEmail(auth.getName());
        user.setEmail(auth.getName() + "(DELETE)");
        user.setPassword(passwordEncoder.encode(PasswordGenerator.generateRandomPassword()));
        user.setAccountNonLocked(false);
        userRepository.save(user);
        return true;
    }

}
