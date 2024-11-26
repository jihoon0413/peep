package com.example.peep.service;

import com.example.peep.config.jwt.JwtToken;
import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.domain.RefreshToken;
import com.example.peep.dto.JwtTokenDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public JwtTokenDto login(String deviceId, StudentDto studentDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(studentDto.userId(), studentDto.userPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return JwtTokenDto.from(jwtTokenProvider.generateToken(authentication, studentDto.userId(), deviceId));

    }

    public JwtTokenDto refreshToken(String deviceId, String userId, JwtTokenDto jwtTokenDto) {

        RefreshToken refresh = refreshTokenRepository.findByUserIdAndDeviceId(userId, deviceId);

        String accessToken = "";

        try {
            if (refresh != null && jwtTokenProvider.validateToken(jwtTokenDto.refreshToken()) && jwtTokenDto.refreshToken().contains(refresh.getToken())) {
                accessToken = jwtTokenProvider.generateAccess(userId);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            log.info("Invalid token");
            return null;
        }
        tokenBlacklistService.addToBlacklist(jwtTokenDto.accessToken(), (new Date()).getTime()+ (1000 * 60 * 30));
        JwtToken jwtToken = new JwtToken("Bearer", accessToken, refresh.getToken(), userId);

        return JwtTokenDto.from(jwtToken);
    }

    public void logout(JwtTokenDto jwtTokenDto) {

        long now = (new Date()).getTime();

        tokenBlacklistService.addToBlacklist(jwtTokenDto.accessToken(), now + (1000 * 60 * 30));

        refreshTokenRepository.deleteByUserIdAndToken(jwtTokenDto.id(), jwtTokenDto.refreshToken());
    }
}
