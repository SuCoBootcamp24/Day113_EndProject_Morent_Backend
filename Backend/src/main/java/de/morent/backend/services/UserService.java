package de.morent.backend.services;

import de.morent.backend.dtos.auth.AuthResponseDTO;
import de.morent.backend.dtos.auth.SignUpRequestDto;
import de.morent.backend.dtos.user.UserDetailsDTO;
import de.morent.backend.dtos.user.UserProfileRequestDTO;
import de.morent.backend.dtos.user.UserProfileResponseDTO;
import de.morent.backend.entities.Address;
import de.morent.backend.entities.Image;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private ProfileRepository profileRepository;
    private AddressRepository addressRepository;
    private GeocodingService geocodingService;
    private ImagesService imagesService;
    private AuthService authService;
    private PasswordEncoder passwordEncoder;
    private VerifyService verifyService;
    private RedisService redisService;
    private TokenService tokenService;

    public UserService(UserRepository userRepository, ProfileRepository profileRepository, AddressRepository addressRepository, GeocodingService geocodingService, ImagesService imagesService, AuthService authService, PasswordEncoder passwordEncoder, VerifyService verifyService, RedisService redisService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.addressRepository = addressRepository;
        this.geocodingService = geocodingService;
        this.imagesService = imagesService;
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
        existingUser.get().setUpdated(LocalDateTime.now());
        userRepository.save(existingUser.get());
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
        profile.setAddress(CreateEmptyAddress());
        user.setRole(UserRole.USER);
        user.setProfile(profile);
        userRepository.save(user);
        verifyService.sendVerifyMail(user.getEmail());
    }

    private Address CreateEmptyAddress() {
        Address address = new Address();
        address.setStreet("Unbekannt");
        address.setHouseNumber("Unbekannt");
        address.setZipCode("Unbekannt");
        address.setCity("Unbekannt");
        address.setCountry("Unbekannt");
        address = addressRepository.save(address);
        return address;
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
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
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
        if (dto.firstName() != null && !dto.firstName().isEmpty()) userProfile.setFirstName(dto.firstName());
        if (dto.lastName() != null && !dto.lastName().isEmpty()) userProfile.setLastName(dto.lastName());
        if (dto.birthDate() != null) userProfile.setDateOfBirth(dto.birthDate());
        if (dto.phoneNumber() != null && !dto.phoneNumber().isEmpty()) userProfile.setPhoneNumber(dto.phoneNumber());

        Address userAddress = userProfile.getAddress();

        if (dto.street() != null && !dto.street().isEmpty()) userAddress.setStreet(dto.street());
        if (dto.houseNumber() != null && !dto.houseNumber().isEmpty()) userAddress.setHouseNumber(dto.houseNumber());
        if (dto.zipCode() != null && !dto.zipCode().isEmpty()) userAddress.setZipCode(dto.zipCode());
        if (dto.city() != null && !dto.city().isEmpty()) userAddress.setCity(dto.city());
        if (dto.country() != null && !dto.country().isEmpty()) userAddress.setCountry(dto.country());
        addressRepository.save(userAddress);

        if (geocodingService.convertAddressToCoordinates(userAddress).length() > 3) userAddress.setRealUserAddress(true);

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

    @Transactional
    public boolean setNewImagesToUserProfile(MultipartFile file, Authentication auth) {
        User user = findUserByEmail(auth.getName());
        Profile userProfile = user.getProfile();

        Image img = imagesService.setImageToUserProfile(userProfile, file);
        if (img!= null) {
            userProfile.setImage(img);
            profileRepository.save(userProfile);
            return true;
        }
        return false;
    }

    public UserDetailsDTO getUserDetails(Authentication auth) {
        User user = findUserByEmail(auth.getName());
        return UserMapper.toUserDetailsDTO(user);
    }
}
