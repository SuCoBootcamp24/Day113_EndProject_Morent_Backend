package de.morent.backend.controller;

import de.morent.backend.services.GeocodingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
