package de.morent.backend.controller;

import de.morent.backend.dtos.auth.AuthResponseDTO;
import de.morent.backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    //POST / signUp




    //POST / login
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(Authentication auth) {
        AuthResponseDTO dto = userService.getTokenByLogin(auth);
        if (dto != null) return new ResponseEntity<>(dto, HttpStatus.OK);
        else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

}
