package de.morent.backend.services;

import de.morent.backend.entities.Address;
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

        //String url = "https://nominatim.openstreetmap.org/search?q=Berner%20heerweg%20151,%20Hamburg,%2022159,%20Deutschland&format=xml&polygon=1&addressdetails=1";
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



}