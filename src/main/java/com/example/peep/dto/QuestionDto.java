package com.example.peep.dto;

import com.example.peep.domain.Question;

public record QuestionDto(
        String content
) {
    public static QuestionDto of(String content) {
        return new QuestionDto(content);
    }

    public static QuestionDto from(Question question) {
        return QuestionDto.of(question.getContent());
    }
}
