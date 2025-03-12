package com.example.peep.controller;

import com.example.peep.dto.StudentDto;
import com.example.peep.dto.response.StudentDetailResponse;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/students")
@RestController
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<StudentDetailResponse> getStudent(@RequestHeader("Authorization") String token,
                                                            @RequestParam("userId") String userId) {
        return studentService.getStudent(token, userId);
    }

    @PostMapping("/new")
    public ResponseEntity<Void> newStudent(@RequestBody StudentDto studentDto) {
        return studentService.newStudent(studentDto);
    }

    @PostMapping("/modify")
    public ResponseEntity<Void> modifyStudent(@RequestHeader("Authorization") String accessToken, @RequestBody StudentDto studentDto) {
        return studentService.modifyStudent(accessToken, studentDto);
    }

    @GetMapping("/delete")
    public ResponseEntity<Void> deleteStudent(@RequestHeader("Authorization") String accessToken) {
        return studentService.deleteStudent(accessToken);
    }

    @GetMapping("/getFourStudents")
    public ResponseEntity<List<StudentResponse>> getFourStudents(@RequestHeader("Authorization") String token,
                                                 @RequestParam("communityId") Long communityId) {
        return studentService.getFourStudents(token, communityId);
    }
}
