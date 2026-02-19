package com.example.peep.service;

import com.example.peep.config.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class TokenBlacklistService {

    private final RedisTemplate<String, String> blackList;
    private final Key key;

    private static final long CODE_EXPIRATION_SECONDS = 30;

    public TokenBlacklistService(RedisTemplate<String, String> blackList, @Value("${spring.jwt.secret}") String secretKey) {
        this.blackList = blackList;
        byte[] secretByteKey = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(secretByteKey);
    }

    public void addToBlacklist(String token) {
        String hashedJti = getHashedJti(token);
        blackList.opsForValue().set(hashedJti, "blacklisted", CODE_EXPIRATION_SECONDS, TimeUnit.MINUTES);
    }

    public boolean isTokenBlacklisted(String token) {
        String hashedJti = getHashedJti(token);
        return Boolean.TRUE.equals(blackList.hasKey(hashedJti));
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getHashedJti(String token) {
        String jti = parseClaims(token).getId();
        return hashString(jti);
    }

    private String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("해싱 알고리즘을 찾을 수 없습니다.");
        }
    }
}
