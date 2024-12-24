package com.example.peep.errors.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.validation.FieldError;

import java.util.List;

public record ErrorResponse(
        String field,
        String message
) {

    public static ErrorResponse of(String field, String message) {
        return new ErrorResponse(field, message);
    }
}
