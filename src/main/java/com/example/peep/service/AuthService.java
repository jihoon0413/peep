package com.example.peep.service;

import com.example.peep.config.jwt.JwtToken;
import com.example.peep.config.jwt.JwtTokenProvider;
import com.example.peep.details.SmsUtil;
import com.example.peep.domain.RefreshToken;
import com.example.peep.domain.Student;
import com.example.peep.domain.enumType.Event;
import com.example.peep.dto.response.JwtTokenResponse;
import com.example.peep.dto.request.LoginRequest;
import com.example.peep.dto.request.VerifyCodeRequestDto;
import com.example.peep.dto.response.Response;
import com.example.peep.errors.ErrorCode;
import com.example.peep.errors.PeepApiException;
import com.example.peep.repository.RefreshTokenRepository;
import com.example.peep.repository.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RecordService recordService;
    private final TokenBlacklistService tokenBlacklistService;
    private final VerificationCodeService verificationCodeService;
    private final SmsUtil smsUtil;

    public JwtTokenResponse login(HttpServletRequest request, LoginRequest login) {

        Student student = studentRepository.findByUserId(login.userId()).orElseThrow(() ->
                new PeepApiException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", login.userId())));

        if(!passwordEncoder.matches(login.password(), student.getUserPassword())) {
            recordService.recordEvent(request, student.getUserId(), Event.LOG_IN_FAIL);
            throw new PeepApiException(ErrorCode.INVALID_PASSWORD);
        }

        recordService.recordEvent(request, student.getUserId(), Event.LOG_IN);

        RefreshToken refreshToken = refreshTokenRepository.findByUserIdAndDeviceId(student.getUserId(), request.getHeader("Device-Id"));
        if(refreshToken != null) {
            throw new PeepApiException(ErrorCode.ALREADY_LOGGED_IN, String.format("%s is already logged in to this device", student.getUserId()));
        }
        return JwtTokenResponse.from(jwtTokenProvider.generateToken(student.getUserId(), request.getHeader("Device-Id")));
    }

    public JwtTokenResponse refreshToken(String deviceId, String token) {

        String refreshToken = token.substring(7);

        RefreshToken refresh = refreshTokenRepository.findByTokenAndDeviceId(refreshToken, deviceId);

        if (refresh != null && jwtTokenProvider.validateToken(refresh.getToken())) {
            JwtToken jwtToken = jwtTokenProvider.generateToken(refresh.getUserId(), deviceId);
            refresh.setToken(jwtToken.getRefreshToken());
            refreshTokenRepository.save(refresh);
            return JwtTokenResponse.from(jwtToken);
        } else {
            throw new PeepApiException(ErrorCode.INVALID_TOKEN);
        }
    }

    public String logout(HttpServletRequest request, String deviceId, String oldAccess, String id) {

        String accessToken = oldAccess.substring(7);

        recordService.recordEvent(request, id, Event.LOG_OUT);

        tokenBlacklistService.addToBlacklist(accessToken);

        refreshTokenRepository.deleteByUserIdAndDeviceId(id, deviceId);

        return "Logout successful";
    }

    public String sendSms(String phone) {
        String phoneNum = phone.replaceAll("-","");

        String verificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);
        //TODO: 비용문제로 주석처리 해놓은 해제시킬 필요 있음
        smsUtil.sendOne(phoneNum, verificationCode);

        verificationCodeService.saveVerificationCode(phone, verificationCode);

        return "Text sent successfully";
    }

    public Boolean checkVerifyCode(VerifyCodeRequestDto verifyCodeRequestDto) {

        boolean result =  verificationCodeService.verifyCode(verifyCodeRequestDto.phoneNumber(), verifyCodeRequestDto.verifyCode());

        if(!result) {
            throw new PeepApiException(ErrorCode.WRONG_VERIFICATION_CODE);
        }
        return true;
    }

}

