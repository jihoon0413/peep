package com.example.peep.controller;

import com.example.peep.config.jwt.JwtToken;
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
    public String newStudent(@RequestBody StudentDto studentDto) {
        studentService.newStudent(studentDto);
        return "success";
    }

    @PostMapping("/login")
    public JwtToken login(@RequestBody StudentDto studentDto) {
         return studentService.login(studentDto);
    }

//    @GetMapping("/refresh")
//    public JwtToken refreshToken() {
//        return studentService.refreshToken();
//    }
}
