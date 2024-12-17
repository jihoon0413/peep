package com.example.peep.controller;

import com.example.peep.domain.mapping.CommunityQuestion;
import com.example.peep.dto.CommunityQuestionDto;
import com.example.peep.dto.StudentDto;
import com.example.peep.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/choice")
    public void chooseStudent(@RequestHeader("Authorization") String token,
                              @RequestParam("userId") String userId,
                              @RequestBody CommunityQuestionDto communityQuestionDto) {
        questionService.chooseStudent(token, userId, communityQuestionDto.id());
    }

    @GetMapping("/getQuestionList")
    public List<CommunityQuestionDto> getQuestionList(@RequestHeader("Authorization") String token) {
        return questionService.getQuestionList(token);
    }
}
