package de.morent.backend.controller;

import de.morent.backend.dtos.auth.AuthResponseDTO;
import de.morent.backend.dtos.auth.SignUpRequestDto;
import de.morent.backend.dtos.auth.VerifyCodeRequestDto;
import de.morent.backend.entities.User;
import de.morent.backend.exceptions.UserAlreadyExistsException;

import de.morent.backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    //POST / signUp
    @PostMapping("/signUp")
    public ResponseEntity<User> newRegistrationUser(@RequestBody SignUpRequestDto dto) {
        try {
            return ResponseEntity.ok(userService.newRegistrationUser(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    //POST / login
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(Authentication auth) {
        AuthResponseDTO dto = userService.getTokenByLogin(auth);
        if (dto != null) return new ResponseEntity<>(dto, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/unlock")
    public ResponseEntity<AuthResponseDTO> unlockAccount(@RequestBody VerifyCodeRequestDto dto){
        try {
        return ResponseEntity.ok(userService.unlockAccount(dto.verifyCode()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
