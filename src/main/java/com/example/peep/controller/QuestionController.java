package com.example.peep.controller;

import com.example.peep.domain.mapping.CommunityQuestion;
import com.example.peep.dto.CommunityQuestionDto;
import com.example.peep.dto.StudentDto;
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

    @PostMapping("/choice")
    public ResponseEntity<Void> chooseStudent(@RequestHeader("Authorization") String token,
                                        @RequestParam("userId") String userId,
                                        @RequestBody CommunityQuestionDto communityQuestionDto) {
        return questionService.chooseStudent(token, userId, communityQuestionDto.id());
    }

    @GetMapping("/getQuestionList")
    public ResponseEntity<List<CommunityQuestionDto>> getQuestionList(@RequestHeader("Authorization") String token) {
        return questionService.getQuestionList(token);
    }
}
