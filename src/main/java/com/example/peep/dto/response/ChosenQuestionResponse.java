package com.example.peep.dto.response;

import com.example.peep.domain.enumType.Gender;
import com.example.peep.domain.enumType.QuestionType;
import com.example.peep.dto.QuestionDto;

import java.time.LocalDateTime;

public record ChosenQuestionResponse(
        Long id,
        QuestionDto questionDto,
        LocalDateTime chosenDate,
        QuestionType type,
        Gender writerGender
) {
    public static ChosenQuestionResponse of(Long id, QuestionDto questionDto, LocalDateTime chosenDate, QuestionType type, Gender gender) {
        return new ChosenQuestionResponse(id, questionDto, chosenDate, type, gender);
    }

}
