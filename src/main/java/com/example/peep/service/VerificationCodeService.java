package com.example.peep.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationCodeService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final long CODE_EXPIRATION_SECONDS = 300; // 5분

    public void saveVerificationCode(String phoneNumber, String code) {
        redisTemplate.opsForValue().set(phoneNumber, code, CODE_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    public boolean verifyCode(String phoneNumber, String inputCode) {
        String storedCode = redisTemplate.opsForValue().get(phoneNumber);
        return storedCode != null && storedCode.equals(inputCode);
    }


}
