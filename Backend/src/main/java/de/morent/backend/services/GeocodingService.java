package de.morent.backend.services;

import de.morent.backend.tools.VowelConverter;
import de.morent.backend.entities.Address;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Service
public class GeocodingService {
    private final RestTemplate restTemplate;
    private final RedisService redisService;

//    public GeocodingService(RestTemplate restTemplate, RedisService redisService) {
//        this.restTemplate = new RestTemplateBuilder()
//                .setConnectTimeout(Duration.ofSeconds(15)) // Verbindungstimeout (z.B. 5 Sekunden)
//                .setReadTimeout(Duration.ofSeconds(15))    // Lese-Timeout (z.B. 5 Sekunden)
//                .build();
//        this.redisService = redisService;
//    }


    public GeocodingService(RestTemplate restTemplate, RedisService redisService) {
        this.restTemplate = restTemplate;
        this.redisService = redisService;
    }

    public String convertAddressToCoordinates(Address newAddress) {
        String street = newAddress.getStreet().replace(" ", "-");
        String address = street + " " + newAddress.getHouseNumber() + ","
                + newAddress.getCity() + ","
                + newAddress.getZipCode() + ","
                + newAddress.getCountry();
        return getCoordinates(address);
    }

    public String getCoordinates(String address) {
        address = VowelConverter.convertString(address);
        if (redisService.hasKey(address)) {
            return redisService.getValue(address);
        }

        String url = UriComponentsBuilder.fromHttpUrl("https://nominatim.openstreetmap.org/search")
                .queryParam("q", address)
                .queryParam("format", "json")
                .queryParam("addressdetails", "1")
                .toUriString();
    try{
        List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);

        if (response != null && !response.isEmpty()) {
            Map<String, Object> location = response.get(0);
            String latitude = (String) location.get("lat");
            String longitude = (String) location.get("lon");

            if (latitude != null && longitude != null) {
                String coordinates = latitude + ", " + longitude;
                // Wert in Redis speichern
                redisService.addLocation(address, Double.parseDouble(latitude),  Double.parseDouble(longitude));
                return coordinates;
            }
        }
    } catch (RestClientException e) {
        // Logge den Fehler und behandle ihn entsprechend
        System.err.println("Fehler bei der Anfrage an die Geokodierungs-API: " + e.getMessage());
    }

        return null;
    }

    public double calcDistance(String fromName, String fromAddress, String toName, String toAddress) {
        if (fromName == null || fromAddress == null || toName == null || toAddress == null) {
            throw new IllegalArgumentException("All parameters must be provided.");
        }
        fromName = VowelConverter.convertStringWithAll(fromName);
        toName = VowelConverter.convertStringWithAll(toName);

        if (!redisService.locationExists(fromName))
            convertStringToLocation(fromName, fromAddress);
        if (!redisService.locationExists(toName))
            convertStringToLocation(toName, toAddress);

        return redisService.getDistance(fromName, toName, Metrics.KILOMETERS);
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