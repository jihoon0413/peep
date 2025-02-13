package com.example.peep.controller;

import com.example.peep.dto.CommunityQuestionDto;
import com.example.peep.dto.StudentQuestionDto;
import com.example.peep.dto.response.HomeQuestionListResponse;
import com.example.peep.dto.response.ChosenQuestionResponse;
import com.example.peep.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/commonChoice/{userId}")
    public ResponseEntity<Void> commonChooseStudent(@RequestHeader("Authorization") String token,
                                        @PathVariable("userId") String userId,
                                        @RequestBody CommunityQuestionDto communityQuestionDto) {
        return questionService.commonChooseStudent(token, userId, communityQuestionDto.id());
    }

    @PostMapping("/randomChoice/{userId}")
    public ResponseEntity<Void> randomChooseStudent(@RequestHeader("Authorization") String token,
                                                    @PathVariable("userId") String uerId,
                                                    @RequestBody StudentQuestionDto studentQuestionDto) {
        return questionService.randomChooseStudent(token, uerId, studentQuestionDto.id());
    }

    @GetMapping("/getQuestionList")
    public ResponseEntity<HomeQuestionListResponse> getQuestionList(@RequestHeader("Authorization") String token) {
        return questionService.getQuestionList(token);
    }

    @GetMapping("/getChosenQuestionList")
    public ResponseEntity<List<ChosenQuestionResponse>> getChosenQuestionList(@RequestHeader("Authorization") String token) {
        return questionService.getChosenQuestionList(token);
    }
}
