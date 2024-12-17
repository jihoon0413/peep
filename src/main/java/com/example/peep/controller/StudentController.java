package com.example.peep.controller;

import com.example.peep.dto.StudentDto;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public void modifyStudent(@RequestHeader("Authorization") String accessToken, @RequestBody StudentDto studentDto) {
        studentService.modifyStudent(accessToken, studentDto);
    }

    @GetMapping("/delete")
    public void deleteStudent(@RequestHeader("Authorization") String accessToken) {
        studentService.deleteStudent(accessToken);
    }

    @GetMapping("/getFourStudents")
    public List<StudentResponse> getFourStudents(@RequestHeader("Authorization") String token,
                                                 @RequestParam("communityId") Long communityId) {
        return studentService.getFourStudents(token, communityId);
    }
}
