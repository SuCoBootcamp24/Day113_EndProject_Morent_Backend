package de.morent.backend.services;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

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
}
