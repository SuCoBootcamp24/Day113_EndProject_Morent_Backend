package de.morent.backend.services;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metric;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private static final String GEO_KEY = "locations";

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, Long> longRedisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate, RedisTemplate<String, Long> longRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.longRedisTemplate = longRedisTemplate;
    }

    public void saveVerifyCode(String key, String value){
        redisTemplate.opsForValue().set(key, value, 15, TimeUnit.MINUTES);
    }

    public String getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteValue(String key){
        redisTemplate.delete(key);
    }

    public void saveClientRequestCount(String key, Long count, Long timeout){
        longRedisTemplate.opsForValue().set(key, count, timeout, TimeUnit.MINUTES);
    }

    public Long getClientRequestCount(String key) {
        return longRedisTemplate.opsForValue().get(key);
    }

    public void incrementCount(String key){
        longRedisTemplate.opsForValue().increment(key, 1L);
    }

    public boolean hasKey(String key){
        return Boolean.TRUE.equals(longRedisTemplate.hasKey(key));
    }


    //-- GEO ----

    public boolean locationExists(String name) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        List<Point> positions = geoOps.position(GEO_KEY, name);
        return positions != null && !positions.isEmpty(); // true, wenn die Location existiert
    }

    public void addLocation(String name, double lat, double lon) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        geoOps.add(GEO_KEY, new Point(lon, lat), name);
    }

    public double getDistance(String location1, String location2, Metric metric) {
        System.out.println("getDistance" + location1 + ": " + location2);

        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();


        Distance distance = geoOps.distance(GEO_KEY, location1, location2, metric);

        System.out.println("Distance " + distance);

        return (distance != null) ? distance.getValue() : 0.0;
    }


    public void deleteAllLocations() {
        redisTemplate.delete(GEO_KEY);
        System.out.println("Alle Locations wurden aus Redis gelöscht.");
    }

}
