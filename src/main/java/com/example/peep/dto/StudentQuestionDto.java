package com.example.peep.dto;

import com.example.peep.domain.mapping.StudentQuestion;
import com.example.peep.dto.response.StudentResponse;

public record StudentQuestionDto(
        StudentResponse writer,
        QuestionDto question,
        boolean whether
) {

    public static StudentQuestionDto from(StudentQuestion studentQuestion) {
        return new StudentQuestionDto(
                StudentResponse.from(studentQuestion.getWriter()),
                QuestionDto.from(studentQuestion.getQuestion()),
                studentQuestion.isWhether()
        );
    }

}
