package com.example.peep.service;

import com.example.peep.config.jwt.JwtToken;
import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.details.SmsUtil;
import com.example.peep.domain.LoginRecord;
import com.example.peep.domain.RefreshToken;
import com.example.peep.dto.Event;
import com.example.peep.dto.JwtTokenDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.dto.request.VerifyCodeRequestDto;
import com.example.peep.repository.LoginRecordRepository;
import com.example.peep.repository.RefreshTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginRecordRepository loginRecordRepository;
    private final TokenBlacklistService tokenBlacklistService;
    private final VerificationCodeService verificationCodeService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SmsUtil smsUtil;

    public JwtTokenDto login(HttpServletRequest request, StudentDto studentDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(studentDto.userId(), studentDto.userPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        recordEvent(request, studentDto.userId(), Event.LOG_IN);

        return JwtTokenDto.from(jwtTokenProvider.generateToken(authentication, studentDto.userId(), request.getHeader("Device-Id")));

    }

    public JwtTokenDto refreshToken(String deviceId, JwtTokenDto jwtTokenDto, String oldAccess) {

        RefreshToken refresh = refreshTokenRepository.findByUserIdAndDeviceId(jwtTokenDto.id(), deviceId);

        String accessToken = "";

        try {
            if (refresh != null && jwtTokenProvider.validateToken(jwtTokenDto.refreshToken()) && jwtTokenDto.refreshToken().contains(refresh.getToken())) {
                accessToken = jwtTokenProvider.generateAccess(jwtTokenDto.id());
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            log.info("Invalid token");
            return null;
        }
        tokenBlacklistService.addToBlacklist(oldAccess);
        JwtToken jwtToken = new JwtToken("Bearer", accessToken, refresh.getToken(), jwtTokenDto.id());

        return JwtTokenDto.from(jwtToken);
    }

    public void logout(HttpServletRequest request, JwtTokenDto jwtTokenDto, String oldAccess) {

        recordEvent(request, jwtTokenDto.id(), Event.LOG_OUT);

        tokenBlacklistService.addToBlacklist(oldAccess);

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

    public void recordEvent(HttpServletRequest request, String userId, Event eventType) {

        String userAgent = getUserAgent(request);
        String clientIp = getIp(request);

        LoginRecord loginRecord = LoginRecord.of(userId, clientIp, userAgent, eventType);

        loginRecordRepository.save(loginRecord);
    }

    public String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

}

