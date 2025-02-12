package com.example.peep.dto.response;

import com.example.peep.dto.CommunityQuestionDto;
import com.example.peep.dto.StudentQuestionDto;

import java.util.List;

public record HomeResponse(
        List<CommunityQuestionDto> commonQuestions,
        List<StudentQuestionDto> randomQuestions
) {
    public static HomeResponse of(List<CommunityQuestionDto> commonQuestions, List<StudentQuestionDto> randomQuestions) {
        return new HomeResponse(commonQuestions, randomQuestions);
    }

}
