package de.morent.backend.controller;

import de.morent.backend.entities.Image;
import de.morent.backend.services.ImagesService;
import de.morent.backend.services.UserService;
import de.morent.backend.services.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
public class ImagesController {

    private UserService userService;
    private VehicleService vehicleService;

    public ImagesController(UserService userService, VehicleService vehicleService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
    }



    //POST /user Create/update UserProfile images
    @PostMapping("/user")
    public ResponseEntity<Void> createUserProfileImages(@RequestParam MultipartFile file, Authentication auth) {
        if (userService.setNewImagesToUserProfile(file, auth)) return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().build();
    }



    //POST /vehicle Create/update Vehicle images
    @PostMapping("/vehicle")
    public ResponseEntity<Void> createVehicleImages(@RequestParam long vehicleId, @RequestParam MultipartFile file) {
       vehicleService.setNewImageToVehicle(vehicleId, file);
        return ResponseEntity.ok().build();
    }


}
