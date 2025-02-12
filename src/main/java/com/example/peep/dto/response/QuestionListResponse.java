package com.example.peep.dto.response;

import java.util.List;

public record QuestionListResponse(
        List<QuestionResponse> questionResponseList
) {
    public static QuestionListResponse of(List<QuestionResponse> questionResponseList) {
        return new QuestionListResponse(questionResponseList);
    }
}
