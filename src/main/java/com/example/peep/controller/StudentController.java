package com.example.peep.controller;

import com.example.peep.dto.StudentDto;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/students")
@RestController
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public StudentResponse getStudent(@RequestParam("userId") String userId) {
        return studentService.getStudent(userId);
    }

    @PostMapping("/new")
    public void newStudent(@RequestBody StudentDto studentDto) {
        studentService.newStudent(studentDto);
    }

    @PostMapping("/modify")
    public void modifyStudent(@RequestBody StudentDto studentDto) {
        studentService.modifyStudent(studentDto);
    }
}
