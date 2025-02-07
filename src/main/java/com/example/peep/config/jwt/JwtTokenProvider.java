package com.example.peep.config.jwt;

import com.example.peep.domain.RefreshToken;
import com.example.peep.repository.RefreshTokenRepository;
import com.example.peep.service.TokenBlacklistService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final long expiration = 1000 * 60 * 30; //30min
    private final long refreshExpiration = 1000 * 60 * 60 * 24 * 7; //1week

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistService tokenBlacklistService;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secretKey, RefreshTokenRepository refreshTokenRepository, TokenBlacklistService tokenBlacklistService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenBlacklistService = tokenBlacklistService;

        byte[] secretByteKey = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(secretByteKey);
    }

    public JwtToken generateToken(Authentication authentication, String id, String deviceId) {

        long now = (new Date()).getTime();
        Date refreshTokenExpiresIn = new Date(now + refreshExpiration);

        String accessToken = generateAccess(authentication.getName());
        String refreshToken = generateRefresh(refreshTokenExpiresIn);

        RefreshToken refresh = RefreshToken.of(authentication.getName(),deviceId, refreshToken, refreshTokenExpiresIn);
        saveRefresh(refresh);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(id)
                .build();
    }

    public String generateAccess(String userId) {

        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + expiration);

        String accessToken = Jwts.builder()
                .setSubject(userId)
                .claim("auth", "ROLE_USER")
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return accessToken;
    }

    public String generateRefresh(Date refreshTokenExpiresIn) {

        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return refreshToken;
    }

    public void saveRefresh(RefreshToken refreshToken){

        RefreshToken refresh = refreshTokenRepository.findByTokenAndDeviceId(refreshToken.getToken(), refreshToken.getDeviceId());

        if(refresh == null) {
            refreshTokenRepository.save(refreshToken);
        } else {
            refresh.setToken(refreshToken.getToken());
            refreshTokenRepository.save(refresh);
        }
    }

    public Authentication getAuthentication(String accessToken) {
        //토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateToken(String token) {
        try {
            if(tokenBlacklistService.isTokenBlacklisted(token)) {
                return false;
            }
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public String getUserId(String token) {

        String bearerToken = token.substring(7);
        Claims claims = parseClaims(bearerToken);

        return claims.getSubject();

//        return claims.getId();
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

}
