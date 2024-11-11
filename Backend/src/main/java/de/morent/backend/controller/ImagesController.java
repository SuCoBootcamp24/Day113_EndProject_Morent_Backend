package de.morent.backend.controller;

import de.morent.backend.entities.Image;
import de.morent.backend.services.ImagesService;
import de.morent.backend.services.UserService;
import de.morent.backend.services.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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
    @PostMapping(value = "/user", consumes = "multipart/form-data")
    public ResponseEntity<Void> createUserProfileImages(@RequestPart("file") MultipartFile file, Authentication auth) {
        if (userService.setNewImagesToUserProfile(file, auth)) return ResponseEntity.ok().build();
        else return ResponseEntity.badRequest().build();
    }



    //POST /vehicle Create/update Vehicle images
    @PostMapping(value = "/vehicle", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> createVehicleImages(@RequestParam("file") MultipartFile file, @RequestParam("vehicleId") long vehicleId) {
       vehicleService.setNewImageToVehicle(vehicleId, file);
        return ResponseEntity.ok().build();
    }


}
