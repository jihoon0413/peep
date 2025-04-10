package com.example.peep.controller;


import com.example.peep.dto.request.PointStudentRequest;
import com.example.peep.dto.response.ChosenQuestionResponse;
import com.example.peep.dto.response.HomeQuestionListResponse;
import com.example.peep.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/commonChoice")
    public ResponseEntity<Void> commonChooseStudent(Authentication authentication,
                                                    @RequestBody PointStudentRequest pointStudentRequest) {
        return questionService.commonChooseStudent(authentication.getName(), pointStudentRequest.studentId(), pointStudentRequest.questionId());
    }

    @PostMapping("/randomChoice")
    public ResponseEntity<Void> randomChooseStudent(Authentication authentication,
                                                    @RequestBody PointStudentRequest pointStudentRequest) {
        return questionService.randomChooseStudent(authentication.getName(), pointStudentRequest.studentId(), pointStudentRequest.questionId());
    }

    @GetMapping("/getQuestionList")
    public ResponseEntity<HomeQuestionListResponse> getQuestionList(Authentication authentication) {
        return questionService.getQuestionList(authentication.getName());
    }

    @GetMapping("/getChosenQuestionList")
    public ResponseEntity<List<ChosenQuestionResponse>> getChosenQuestionList(Authentication authentication) {
        return questionService.getChosenQuestionList(authentication.getName());
    }
}
