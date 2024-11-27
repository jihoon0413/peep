package com.example.peep.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {

    @Autowired
    private RedisTemplate<String, String> blackList;

    private static final long CODE_EXPIRATION_SECONDS = 30;

    public void addToBlacklist(String token) {
        blackList.opsForValue().set(token, "blacklisted", CODE_EXPIRATION_SECONDS, TimeUnit.MINUTES);
    }

    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(blackList.hasKey(token));
    }
}
