package de.morent.backend.controller;

import de.morent.backend.services.GeocodingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/geo")
public class GeoController {

    private GeocodingService geocodingService;

    public GeoController(GeocodingService geocodingService) {
        this.geocodingService = geocodingService;
    }

    @GetMapping
    public String getCoordinates(String address) {
        return geocodingService.getCoordinates(address);
    }

    @PostMapping("/distance")
    public Double getDistance(@RequestParam String fromName, @RequestParam String address1, @RequestParam String toName, @RequestParam String address2) {
        geocodingService.calcDistance(fromName,address1, toName, address2);
        return null;
    }

    @DeleteMapping("/delete")
    public void deleteLocations() {
        geocodingService.deleteLocation();
    }
}
