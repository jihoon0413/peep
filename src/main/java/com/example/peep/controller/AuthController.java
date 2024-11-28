package com.example.peep.controller;

import com.example.peep.dto.JwtTokenDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.dto.requestDto.VerifyCodeRequestDto;
import com.example.peep.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public JwtTokenDto login(@RequestHeader("Device-Id") String deviceId,
                             @RequestBody StudentDto studentDto) {
        return authService.login(deviceId, studentDto);
    }

    @PostMapping("/refresh")
    public JwtTokenDto refreshToken(@RequestHeader("Authorization") String accessToken,
                                    @RequestHeader("Device-Id") String deviceId,
                                    @RequestBody JwtTokenDto jwtTokenDto,
                                    @RequestParam("userId") String userId) {

        String oldAccess = accessToken.substring(7);
        return authService.refreshToken(deviceId, userId, jwtTokenDto, oldAccess);
    }

    @PostMapping("/logout")
    public String logout(@RequestBody JwtTokenDto jwtTokenDto,
                         @RequestHeader("Authorization") String accessToken) {

        String oldAccess = accessToken.substring(7);

        authService.logout(jwtTokenDto, oldAccess);
        return "Logout successful";
    }

    @PostMapping("/sendCode")
    public ResponseEntity<?> SendSMS(@RequestBody VerifyCodeRequestDto verifyCodeRequestDto){
        return authService.sendSms(verifyCodeRequestDto.phoneNumber());
    }

    @PostMapping("/verifyCode")
    public boolean checkVerifyCode(@RequestBody VerifyCodeRequestDto verifyCodeRequestDto){
        return authService.checkVerifyCode(verifyCodeRequestDto);
    }
}
