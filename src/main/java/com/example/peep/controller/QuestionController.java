package com.example.peep.controller;


import com.example.peep.dto.request.HintRequest;
import com.example.peep.dto.request.PointStudentRequest;
import com.example.peep.dto.response.ChosenQuestionResponse;
import com.example.peep.dto.response.HomeQuestionListResponse;
import com.example.peep.dto.response.Response;
import com.example.peep.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/choiceCommon")
    public Response<String> commonChooseStudent(Authentication authentication,
                                                    @RequestBody PointStudentRequest pointStudentRequest) {
        return Response.success(questionService.commonChooseStudent(authentication.getName(), pointStudentRequest.studentId(), pointStudentRequest.questionId()));
    }

    @PostMapping("/choiceRandom")
    public Response<String> randomChooseStudent(Authentication authentication,
                                                    @RequestBody PointStudentRequest pointStudentRequest) {
        return Response.success(questionService.randomChooseStudent(authentication.getName(), pointStudentRequest.studentId(), pointStudentRequest.questionId()));
    }

    @GetMapping("/getQuestionList")
    public Response<HomeQuestionListResponse> getQuestionList(Authentication authentication) {
        return Response.success(questionService.getQuestionList(authentication.getName()));
    }

    @GetMapping("/getChosenQuestionList")
    public Response<List<ChosenQuestionResponse>> getChosenQuestionList(Authentication authentication) {
        return Response.success(questionService.getChosenQuestionList(authentication.getName()));
    }

    @PostMapping("/getHintCommonQuestionWriter")
    public Response<String> getCommonQuestionWriterHint(Authentication authentication,
                                                        @RequestBody HintRequest hintRequest) {
        return Response.success(questionService.getCommonQuestionWriterHint(authentication.getName(), hintRequest.questionId()));
    }

    @PostMapping("/getHintRandomQuestionWriter")
    public Response<String> getRandomQuestionWriterHint(Authentication authentication,
                                                        @RequestBody HintRequest hintRequest) {
        return Response.success(questionService.getRandomQuestionWriterHint(authentication.getName(), hintRequest.questionId()));
    }

}
