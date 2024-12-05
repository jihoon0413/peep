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
    public JwtTokenDto login(HttpServletRequest request,
                             @RequestBody StudentDto studentDto) {
        return authService.login(request, studentDto);
    }

    @PostMapping("/refresh")
    public JwtTokenDto refreshToken(@RequestHeader("Authorization") String accessToken,
                                    @RequestHeader("Device-Id") String deviceId,
                                    @RequestBody JwtTokenDto jwtTokenDto,
                                    @RequestParam("userId") String userId) {

        String oldAccess = accessToken.substring(7);
        return authService.refreshToken(deviceId, jwtTokenDto, oldAccess);
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request,
                         @RequestBody JwtTokenDto jwtTokenDto,
                         @RequestHeader("Authorization") String accessToken) {

        String oldAccess = accessToken.substring(7);

        authService.logout(request, jwtTokenDto, oldAccess);
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
