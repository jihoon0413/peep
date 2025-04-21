package com.example.peep.controller;

import com.example.peep.dto.request.LoginRequest;
import com.example.peep.dto.request.VerifyCodeRequestDto;
import com.example.peep.dto.response.JwtTokenResponse;
import com.example.peep.dto.response.Response;
import com.example.peep.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Response<JwtTokenResponse> login(HttpServletRequest request,
                                            @RequestBody LoginRequest loginRequest) {
        return Response.success(authService.login(request, loginRequest));
    }

    @PostMapping("/refresh")
    public Response<JwtTokenResponse> refreshToken(
                                    @RequestHeader("Device-Id") String deviceId,
                                    @RequestHeader("refresh-token") String refreshToken,
                                    Authentication authentication) {
        return Response.success(authService.refreshToken(deviceId, refreshToken));
    }

    @PostMapping("/logout")
    public Response<String> logout(HttpServletRequest request,
                                         @RequestHeader("Device-Id") String deviceId,
                                         @RequestHeader("Authorization") String accessToken,
                                         Authentication authentication) {

        return Response.success(authService.logout(request, deviceId, accessToken, authentication.getName()));

    }

    @PostMapping("/sendCode")
    public Response<String> SendSMS(@RequestBody VerifyCodeRequestDto verifyCodeRequestDto){
        return Response.success(authService.sendSms(verifyCodeRequestDto.phoneNumber()));
    }

    @PostMapping("/verifyCode")
    public Response<Boolean> checkVerifyCode(@RequestBody VerifyCodeRequestDto verifyCodeRequestDto){
        return Response.success(authService.checkVerifyCode(verifyCodeRequestDto));
    }
}
