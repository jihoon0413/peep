package com.example.peep.dto.response;

import com.example.peep.dto.CommunityQuestionDto;
import com.example.peep.dto.StudentQuestionDto;

import java.util.List;

public record HomeQuestionListResponse(
        List<CommunityQuestionDto> commonQuestions,
        List<StudentQuestionDto> randomQuestions
) {
    public static HomeQuestionListResponse of(List<CommunityQuestionDto> commonQuestions, List<StudentQuestionDto> randomQuestions) {
        return new HomeQuestionListResponse(commonQuestions, randomQuestions);
    }

}
