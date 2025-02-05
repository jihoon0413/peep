package com.example.peep.service;

import com.example.peep.config.jwt.JwtToken;
import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.details.SmsUtil;
import com.example.peep.domain.LoginRecord;
import com.example.peep.domain.RefreshToken;
import com.example.peep.domain.enumType.Event;
import com.example.peep.dto.JwtTokenDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.dto.request.VerifyCodeRequestDto;
import com.example.peep.errors.errorcode.CustomErrorCode;
import com.example.peep.errors.exception.RestApiException;
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

import java.util.Date;

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

    public ResponseEntity<JwtTokenDto> refreshToken(String deviceId, String token) {

        String refreshToken = token.substring(7);

        RefreshToken refresh = refreshTokenRepository.findByTokenAndDeviceId(refreshToken, deviceId);

        String accessToken = "";
        String newRefresh = "";

        if (refresh != null && jwtTokenProvider.validateToken(refresh.getToken())) {
            accessToken = jwtTokenProvider.generateAccess(refresh.getUserId());
            long now = (new Date()).getTime();
            newRefresh = jwtTokenProvider.generateRefresh(new Date(now + 1000 * 60 * 60 * 24 * 7));
            refresh.setToken(newRefresh);
            refreshTokenRepository.save(refresh);
        } else {
            throw new RestApiException(CustomErrorCode.INVALID_PARAMETER);
        }

//        tokenBlacklistService.addToBlacklist(oldAccess);
        JwtToken jwtToken = new JwtToken("Bearer", accessToken, newRefresh, refresh.getUserId());

        return ResponseEntity.ok(JwtTokenDto.from(jwtToken));
    }

    public ResponseEntity<String> logout(HttpServletRequest request, String deviceId, String oldAccess) {

        String accessToken = oldAccess.substring(7);
        String id = jwtTokenProvider.getUserId(oldAccess);

        recordEvent(request, id, Event.LOG_OUT);

        tokenBlacklistService.addToBlacklist(accessToken);

        refreshTokenRepository.deleteByUserIdAndDeviceId(id, deviceId);

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

        if(!result) {
            throw new RestApiException(CustomErrorCode.WRONG_VERIFICATION_CODE);
        }
        return ResponseEntity.ok(true);
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

