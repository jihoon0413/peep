package com.example.peep.controller;

import com.example.peep.dto.JwtTokenDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/students")
@RestController
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public String getStudent(@RequestParam("userId") String userId) {
        return studentService.getStudent(userId);
    }

    @PostMapping("/new")
    public void newStudent(@RequestBody StudentDto studentDto) {
        studentService.newStudent(studentDto);
    }

    //TODO: authController 를 만들어 분리하기

    @PostMapping("/login")
    public JwtTokenDto login( @RequestHeader("Device-Id") String deviceId,
                           @RequestBody StudentDto studentDto) {
         return studentService.login(deviceId, studentDto);
    }

    @PostMapping("/refresh")
    public JwtTokenDto refreshToken(@RequestHeader("Device-Id") String deviceId,
                                 @RequestBody JwtTokenDto jwtTokenDto,
                                 @RequestParam("userId") String userId) {

        return studentService.refreshToken(deviceId, userId, jwtTokenDto);
    }

    @PostMapping("/logout")
    public String logout(@RequestBody JwtTokenDto jwtTokenDto) {

        studentService.logout(jwtTokenDto);
        return "Logout successful";
    }
}
