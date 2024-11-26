package com.example.peep.controller;

import com.example.peep.dto.JwtTokenDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public JwtTokenDto refreshToken(@RequestHeader("Device-Id") String deviceId,
                                    @RequestBody JwtTokenDto jwtTokenDto,
                                    @RequestParam("userId") String userId) {

        return authService.refreshToken(deviceId, userId, jwtTokenDto);
    }

    @PostMapping("/logout")
    public String logout(@RequestBody JwtTokenDto jwtTokenDto) {

        authService.logout(jwtTokenDto);
        return "Logout successful";
    }


}
