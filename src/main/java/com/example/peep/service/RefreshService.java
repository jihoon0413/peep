package com.example.peep.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshService {

    private final RedisTemplate<String, String> refreshTokens;

    private static final long CODE_EXPIRATION_DAY = 7;

    public void addRefreshToken(String key, String token) {
        refreshTokens.opsForValue().set(key, token,CODE_EXPIRATION_DAY, TimeUnit.DAYS);
    }

    public String getRefreshToken(String key) {
        return refreshTokens.opsForValue().get(key);
    }

    public void deleteRefreshToken(String key) {
        refreshTokens.delete(key);
    }

}
