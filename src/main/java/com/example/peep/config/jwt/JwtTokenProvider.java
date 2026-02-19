package com.example.peep.config.jwt;

import com.example.peep.errors.ErrorCode;
import com.example.peep.errors.PeepApiException;
import com.example.peep.service.RefreshService;
import com.example.peep.service.TokenBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    private final Key key;
    private final long expiration = 1000 * 60 * 30; //30min
//    private final long expiration = 1000 * 30; //30sec
    private final long refreshExpiration = 1000 * 60 * 60 * 24 * 7; //1week

    private final TokenBlacklistService tokenBlacklistService;
    private final RefreshService refreshService;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secretKey, TokenBlacklistService tokenBlacklistService, RefreshService refreshService) {
        this.tokenBlacklistService = tokenBlacklistService;
        this.refreshService = refreshService;

        byte[] secretByteKey = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(secretByteKey);
    }

    public JwtToken generateToken(String userId, String deviceId) {

        long now = (new Date()).getTime();
        Date refreshTokenExpiresIn = new Date(now + refreshExpiration);

        String accessToken = generateAccess(userId);
        String refreshToken = generateRefresh(userId, refreshTokenExpiresIn);

        saveRefresh(userId, deviceId, refreshToken);

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(userId)
                .build();
    }

    public String generateAccess(String userId) {

        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + expiration);

        String jti = UUID.randomUUID().toString();

        String accessToken = Jwts.builder()
                .setSubject(userId)
                .id(jti)
                .claim("auth", "ROLE_USER")
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return accessToken;
    }

    public String generateRefresh(String userId, Date refreshTokenExpiresIn) {

        String refreshToken = Jwts.builder()
                .setSubject(userId)
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return refreshToken;
    }

    public void saveRefresh(String userId, String deviceId, String refresh){

        String key = userId + ":" + deviceId;
        refreshService.addRefreshToken(key, refresh);

    }

    public Authentication getAccessAuthentication(String accessToken) {
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

    public Authentication getRefreshAuthentication(String refreshToken) {
        //토큰 복호화
        Claims claims = parseClaims(refreshToken);

        UserDetails principal = new User(claims.getSubject(), "", Collections.emptyList());
        return new UsernamePasswordAuthenticationToken(principal, "", Collections.emptyList());
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
            throw new PeepApiException(ErrorCode.INVALID_TOKEN, "Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new PeepApiException(ErrorCode.INVALID_TOKEN, "Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new PeepApiException(ErrorCode.INVALID_TOKEN, "Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new PeepApiException(ErrorCode.INVALID_TOKEN, "JWT claims string is empty");
        }
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
