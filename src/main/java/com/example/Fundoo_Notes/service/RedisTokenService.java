package com.example.Fundoo_Notes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenService {

    private static final String JWT_PREFIX = "jwt:";
    private static final String OTP_PREFIX = "otp:";

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    public void cacheJwt(String token, String email, long ttlSeconds) {
        if (redisTemplate == null) {
            return;
        }
        redisTemplate.opsForValue().set(JWT_PREFIX + token, email, ttlSeconds, TimeUnit.SECONDS);
    }

    public boolean isJwtCached(String token) {
        if (redisTemplate == null) {
            return true;
        }
        return Boolean.TRUE.equals(redisTemplate.hasKey(JWT_PREFIX + token));
    }

    public void cacheOtp(String email, String otp, long ttlSeconds) {
        if (redisTemplate == null) {
            return;
        }
        redisTemplate.opsForValue().set(OTP_PREFIX + email, otp, ttlSeconds, TimeUnit.SECONDS);
    }

    public String getOtp(String email) {
        if (redisTemplate == null) {
            return null;
        }
        return redisTemplate.opsForValue().get(OTP_PREFIX + email);
    }
}
