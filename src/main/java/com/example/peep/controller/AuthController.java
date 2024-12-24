package com.example.peep.controller;

import com.example.peep.dto.JwtTokenDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.dto.request.VerifyCodeRequestDto;
import com.example.peep.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> login(HttpServletRequest request,
                             @RequestBody StudentDto studentDto) {
        return authService.login(request, studentDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtTokenDto> refreshToken(
                                    @RequestHeader("Device-Id") String deviceId,
                                    @RequestBody JwtTokenDto jwtTokenDto) {

        String oldAccess = jwtTokenDto.accessToken().substring(7);
        return authService.refreshToken(deviceId, jwtTokenDto, oldAccess);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request,
                         @RequestBody JwtTokenDto jwtTokenDto,
                         @RequestHeader("Authorization") String accessToken) {

        String oldAccess = accessToken.substring(7);

        return authService.logout(request, jwtTokenDto, oldAccess);

    }

    @PostMapping("/sendCode")
    public ResponseEntity<String> SendSMS(@RequestBody VerifyCodeRequestDto verifyCodeRequestDto){
        return authService.sendSms(verifyCodeRequestDto.phoneNumber());
    }

    @PostMapping("/verifyCode")
    public ResponseEntity<Boolean> checkVerifyCode(@RequestBody VerifyCodeRequestDto verifyCodeRequestDto){
        return authService.checkVerifyCode(verifyCodeRequestDto);
    }
}
