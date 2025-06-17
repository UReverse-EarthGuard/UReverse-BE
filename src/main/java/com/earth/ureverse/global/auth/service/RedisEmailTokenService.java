package com.earth.ureverse.global.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisEmailTokenService {

    private final StringRedisTemplate redisTemplate;

    public void saveToken(String token, String email, Duration ttl) {
        String key = "email:verify:" + token;
        redisTemplate.opsForValue().set(key, email, ttl);
    }

    public String getEmailByToken(String token) {
        return redisTemplate.opsForValue().get("email:verify:" + token);
    }

    public void deleteToken(String token) {
        redisTemplate.delete("email:verify:" + token);
    }
}