package com.example.peep.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {

    private final ConcurrentHashMap<String, Long> blacklist = new ConcurrentHashMap<>();

    // 토큰을 블랙리스트에 추가
    public void addToBlacklist(String token, long expirationTime) {
        blacklist.put(token, expirationTime);
    }

    // 블랙리스트에서 토큰 확인
    public boolean isTokenBlacklisted(String token) {
        Long expirationTime = blacklist.get(token);
        if (expirationTime == null || expirationTime < System.currentTimeMillis()) {
            blacklist.remove(token); // 만료된 블랙리스트 항목 제거
            return false;
        }
        return true;
    }

    @Scheduled(fixedRate = 1000 * 60 * 60) // 1시간마다 실행
    public void removeExpiredTokens() {
        long now = System.currentTimeMillis();
        blacklist.entrySet().removeIf(entry -> entry.getValue() < now);
        System.out.println("Expired tokens removed from blacklist");
    }

}
