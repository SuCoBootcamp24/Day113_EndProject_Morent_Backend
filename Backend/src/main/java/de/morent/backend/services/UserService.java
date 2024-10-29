package de.morent.backend.services;

import de.morent.backend.entities.User;
import de.morent.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Optional<User> findUserById(long userId) {
        return userRepository.findById(userId);
    }
}
