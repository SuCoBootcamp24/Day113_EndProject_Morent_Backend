package de.morent.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class GeocodingService {
    private final RestTemplate restTemplate;

    public GeocodingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getCoordinates(String address) {
        String url = UriComponentsBuilder.fromHttpUrl("https://nominatim.openstreetmap.org/search")
                .queryParam("q", address)
                .queryParam("format", "json")
                .queryParam("addressdetails", "1")
                .toUriString();


        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
        if (response != null && !response.isEmpty()) {
            Map<String, Object> location = response.get(0);
            String coords = location.get("lat") + ", " + location.get("lon");
            return coords;
        }
        return null;
    }



}