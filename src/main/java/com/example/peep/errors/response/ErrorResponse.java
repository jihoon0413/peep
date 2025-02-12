package com.example.peep.errors.response;

public record ErrorResponse(
        String field,
        String message
) {

    public static ErrorResponse of(String field, String message) {
        return new ErrorResponse(field, message);
    }
}
