package de.morent.backend.services;

import de.morent.backend.entities.Address;
import org.springframework.data.geo.Metric;
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


    public void calcDistance(String fromname, String fromAddress,String toname, String toAddress) {
        double [] fromcoords = extracted(fromAddress);

        double [] tocoords = extracted(toAddress);

        redisService.addLocation(fromname, fromcoords[0], fromcoords[1]);
        redisService.addLocation(toname, tocoords[0], tocoords[1]);

        Double distance = redisService.getDistance(fromname, toname, Metrics.KILOMETERS);
        System.out.println(distance);

    }

    private double [] extracted(String fromAddress) {
        String[] fromParts = fromAddress.split(",");
        if (fromParts.length != 2) {
            throw new IllegalArgumentException("Invalid coordinate format. Anticipated: 'latitude, longitude'.");
        }
        try {
            double fromAddLat = Double.parseDouble(fromParts[0].trim());
            double fromAddlon = Double.parseDouble(fromParts[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Koordinaten konnten nicht geparst werden.", e);
        }
    }
}