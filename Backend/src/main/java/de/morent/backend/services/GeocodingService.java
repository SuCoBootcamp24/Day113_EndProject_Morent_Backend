package de.morent.backend.services;

import de.morent.backend.entities.Address;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@Service
public class GeocodingService {
    private final RestTemplate restTemplate;
    private RedisService redisService;

    public GeocodingService(RestTemplate restTemplate, RedisService redisService) {
        this.restTemplate = restTemplate;
        this.redisService = redisService;
    }



    public String convertAddToCoords(Address newAddress) {
        String street = newAddress.getStreet().replace(" ", "-");
        String address = street + " " + newAddress.getHouseNumber() + ","
                + newAddress.getCity() + ","
                + newAddress.getZipCode() + ","
                + newAddress.getCountry();
        System.out.println(address);
        return getCoordinates(address);
    }




    public String getCoordinates(String address) {
        if (redisService.hasKey(address)) {
            return redisService.getValue(address);
        }

        String url = UriComponentsBuilder.fromHttpUrl("https://nominatim.openstreetmap.org/search")
                .queryParam("q", address)
                .queryParam("format", "json")
                .queryParam("addressdetails", "1")
                .toUriString();

         System.out.println(url);
        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

        System.out.println(response);

        if (response != null && !response.isEmpty()) {
            Map<String, Object> location = response.get(0);
            String coords = location.get("lat") + ", " + location.get("lon");
            return coords;
        }
        return null;
    }


    public double calcDistance(String fromName, String fromAddress,String toName, String toAddress) {
        if (fromName == null || fromAddress == null || toName == null || toAddress == null) {
            throw new IllegalArgumentException("All parameters must be provided.");
        }

        if (!redisService.locationExists(fromName))
            convertStringToLocation(fromName, fromAddress);
        if (!redisService.locationExists(toName))
            convertStringToLocation(toName, toAddress);

        Double distance = redisService.getDistance(fromName, toName, Metrics.KILOMETERS);
        System.out.println(distance);
        return distance;
    }

    private void convertStringToLocation(String name, String address) {
        String[] parts = address.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid coordinate format. Anticipated: 'latitude, longitude'.");
        }
        try {
            double latitude = Double.parseDouble(parts[0].trim());
            double longitude = Double.parseDouble(parts[1].trim());
            redisService.addLocation(name, latitude, longitude);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Coordinates could not be parsed", e);
        }
    }
}