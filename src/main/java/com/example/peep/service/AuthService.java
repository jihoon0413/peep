package com.example.peep.service;

import com.example.peep.config.jwt.JwtToken;
import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.details.SmsUtil;
import com.example.peep.domain.RefreshToken;
import com.example.peep.dto.JwtTokenDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.dto.requestDto.VerifyCodeRequestDto;
import com.example.peep.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final VerificationCodeService verificationCodeService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SmsUtil smsUtil;

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
        tokenBlacklistService.addToBlacklist(jwtTokenDto.accessToken());
        JwtToken jwtToken = new JwtToken("Bearer", accessToken, refresh.getToken(), userId);

        return JwtTokenDto.from(jwtToken);
    }

    public void logout(JwtTokenDto jwtTokenDto) {

        long now = (new Date()).getTime();

        tokenBlacklistService.addToBlacklist(jwtTokenDto.accessToken());

        refreshTokenRepository.deleteByUserIdAndToken(jwtTokenDto.id(), jwtTokenDto.refreshToken());
    }

    public ResponseEntity<?> sendSms(String phone) {
        String phoneNum = phone.replaceAll("-","");

        String verificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);
        //TODO: 비용문제로 주석처리 해놓은 해제시킬 필요 있음
        smsUtil.sendOne(phoneNum, verificationCode);

        verificationCodeService.saveVerificationCode(phone, verificationCode);

        return new ResponseEntity<>("Text sent successfully", HttpStatus.OK);
    }

    public boolean checkVerifyCode(VerifyCodeRequestDto verifyCodeRequestDto) {

        return verificationCodeService.verifyCode(verifyCodeRequestDto.phoneNumber(), verifyCodeRequestDto.verifyCode());
    }
}
