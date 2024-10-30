package de.morent.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class ProtectRouteController {

    @GetMapping("/protectRoute")
    public ResponseEntity<String> getUsername(Authentication authentication){
        return ResponseEntity.ok(authentication.getName());
    }
}
