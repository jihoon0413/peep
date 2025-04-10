package com.example.peep.controller;

import com.example.peep.dto.StudentDto;
import com.example.peep.dto.response.StudentDetailResponse;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/students")
@RestController
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<StudentDetailResponse> getStudent(Authentication authentication,
                                                            @RequestParam("userId") String userId) {
        return studentService.getStudent(authentication.getName(), userId);
    }

    @PostMapping("/new")
    public ResponseEntity<Void> newStudent(@RequestBody StudentDto studentDto) {
        return studentService.newStudent(studentDto);
    }

    @PostMapping("/modify")
    public ResponseEntity<Void> modifyStudent(Authentication authentication, @RequestBody StudentDto studentDto) {
        return studentService.modifyStudent(authentication.getName(), studentDto);
    }

    @GetMapping("/delete")
    public ResponseEntity<Void> deleteStudent(Authentication authentication) {
        return studentService.deleteStudent(authentication.getName());
    }

    @GetMapping("/getFourStudents")
    public ResponseEntity<List<StudentResponse>> getFourStudents(Authentication authentication,
                                                 @RequestParam("communityId") Long communityId) {
        return studentService.getFourStudents(authentication.getName(), communityId);
    }
}
