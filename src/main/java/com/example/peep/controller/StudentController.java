package com.example.peep.controller;

import com.example.peep.dto.StudentDto;
import com.example.peep.dto.request.UserIdRequest;
import com.example.peep.dto.response.Response;
import com.example.peep.dto.response.StudentDetailResponse;
import com.example.peep.dto.response.StudentResponse;
import com.example.peep.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/students")
@RestController
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public Response<StudentDetailResponse> getStudent(Authentication authentication,
                                                            @RequestParam("userId") String userId) {
        return Response.success(studentService.getStudent(authentication.getName(), userId));
    }

    @PostMapping("/new")
    public Response<String> newStudent(@RequestBody StudentDto studentDto) {
        return Response.success(studentService.newStudent(studentDto));
    }

    @PostMapping("/isDuplicated")
    public Response<Boolean> isDuplicated(@RequestBody UserIdRequest userId) {
        return Response.success(studentService.isDuplicated(userId.userId()));
    }


    @PostMapping("/modify")
    public Response<String> modifyStudent(Authentication authentication, @RequestBody StudentDto studentDto) {
        return Response.success(studentService.modifyStudent(authentication.getName(), studentDto));
    }

    @PostMapping("/delete")
    public Response<String> deleteStudent(Authentication authentication) {
        return Response.success(studentService.deleteStudent(authentication.getName()));
    }
//  TODO: 탈퇴계정 복구 방법 고민  @PostMapping("/restore")

    @GetMapping("/getFourStudentsInCommunity")
    public Response<List<StudentResponse>> getFourStudentsInCommunity(Authentication authentication,
                                                 @RequestParam("communityId") Long communityId) {
        return Response.success(studentService.getFourStudentsInCommunity(authentication.getName(), communityId));
    }

    @GetMapping("/getFourStudentsInMyFollowing")
    public Response<List<StudentResponse>> getStudentInMyFollowing(Authentication authentication) {
        return Response.success(studentService.getFourStudentsInMyFollowing(authentication.getName()));

    }

}
