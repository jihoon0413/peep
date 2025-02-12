package com.example.peep.dto;

import com.example.peep.domain.mapping.StudentQuestion;

public record StudentQuestionDto(
        QuestionDto questionDto,
        boolean whether
) {

    public static StudentQuestionDto from(StudentQuestion studentQuestion) {
        return new StudentQuestionDto(
                QuestionDto.from(studentQuestion.getQuestion()),
                studentQuestion.isWhether()
        );
    }

}
