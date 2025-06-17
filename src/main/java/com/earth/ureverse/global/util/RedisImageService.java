package com.earth.ureverse.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisImageService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveTempImage(String imageKey, String accessUrl, Duration ttl) {
        redisTemplate.opsForValue().set(imageKey, accessUrl, ttl);
    }

    public Set<String> getAllImageKeys() {
        return redisTemplate.keys("uploads/*");
    }

    public String getAccessUrl(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
}
