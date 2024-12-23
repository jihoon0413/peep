package com.example.peep.service;

import com.example.peep.config.jwt.JwtToken;
import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.details.SmsUtil;
import com.example.peep.domain.LoginRecord;
import com.example.peep.domain.RefreshToken;
import com.example.peep.enumType.Event;
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

    public ResponseEntity<JwtTokenDto> login(HttpServletRequest request, StudentDto studentDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(studentDto.userId(), studentDto.userPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        recordEvent(request, studentDto.userId(), Event.LOG_IN);

        JwtTokenDto jwtTokenDto = JwtTokenDto.from(jwtTokenProvider.generateToken(authentication, studentDto.userId(), request.getHeader("Device-Id")));

        return ResponseEntity.ok(jwtTokenDto);
    }

    public ResponseEntity<JwtTokenDto> refreshToken(String deviceId, JwtTokenDto jwtTokenDto, String oldAccess) {

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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        tokenBlacklistService.addToBlacklist(oldAccess);
        JwtToken jwtToken = new JwtToken("Bearer", accessToken, refresh.getToken(), jwtTokenDto.id());

        return ResponseEntity.ok(JwtTokenDto.from(jwtToken));
    }

    public ResponseEntity<String> logout(HttpServletRequest request, JwtTokenDto jwtTokenDto, String oldAccess) {

        recordEvent(request, jwtTokenDto.id(), Event.LOG_OUT);

        tokenBlacklistService.addToBlacklist(oldAccess);

        refreshTokenRepository.deleteByUserIdAndToken(jwtTokenDto.id(), jwtTokenDto.refreshToken());

        return ResponseEntity.ok("Logout successful");
    }

    public ResponseEntity<String> sendSms(String phone) {
        String phoneNum = phone.replaceAll("-","");

        String verificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);
        //TODO: 비용문제로 주석처리 해놓은 해제시킬 필요 있음
        smsUtil.sendOne(phoneNum, verificationCode);

        verificationCodeService.saveVerificationCode(phone, verificationCode);

        return ResponseEntity.ok("Text sent successfully");
    }

    public ResponseEntity<Boolean> checkVerifyCode(VerifyCodeRequestDto verifyCodeRequestDto) {

        boolean result =  verificationCodeService.verifyCode(verifyCodeRequestDto.phoneNumber(), verifyCodeRequestDto.verifyCode());

        return ResponseEntity.ok(result);
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

