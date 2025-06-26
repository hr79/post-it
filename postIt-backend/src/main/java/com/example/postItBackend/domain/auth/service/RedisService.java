package com.example.postItBackend.domain.auth.service;

import com.example.postItBackend.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(String username, String refreshToken) {
        redisTemplate.opsForValue().set(username, refreshToken, Duration.ofMillis(JwtUtil.REFRESH_TOKEN_EXPIRE_TIME));
    }

    public String getRefreshToken(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void logoutAndBlackListToken(String accessToken, Long expirationTime) {
        redisTemplate.opsForValue().set("blacklist:access:"+accessToken, "logout", expirationTime, TimeUnit.MILLISECONDS);
    }

    public void delete(String username) {
        redisTemplate.delete(username);
    }
}
