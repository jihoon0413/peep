package com.example.peep.dto.response;

import com.example.peep.domain.enumType.QuestionType;
import com.example.peep.dto.QuestionDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record QuestionResponse(
        Long id,
        QuestionDto questionDto,
        LocalDateTime chosenDate,
        QuestionType type
) {
    public static QuestionResponse of(Long id, QuestionDto questionDto, LocalDateTime chosenDate, QuestionType type) {
        return new QuestionResponse(id, questionDto, chosenDate, type);
    }

}
