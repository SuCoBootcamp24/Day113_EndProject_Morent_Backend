package de.morent.backend.controller;

import de.morent.backend.dtos.user.UserProfileRequestDTO;
import de.morent.backend.dtos.user.UserProfileResponseDTO;
import de.morent.backend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //PUT /update update UserProfile
    @PutMapping("/update")
    public ResponseEntity<UserProfileResponseDTO> updateUserProfile(@RequestBody UserProfileRequestDTO dto, Authentication auth) {
        return ResponseEntity.ok(userService.updateUserProfile(dto, auth));
    }

    //PUT /update/password Update Userpassword
    @PutMapping("/update/password")
    public ResponseEntity<Void> updateUserPassword(String newPassword, Authentication auth) {
        // Implement password change logic here
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(Authentication auth) {
        if (userService.deleteUser(auth)) return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().build();
        //JWT must be deleted in the frontend!!
    }

}
