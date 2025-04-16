package com.example.peep.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class PeepApiException extends RuntimeException{

    private final ErrorCode errorCode;
    private final String message;

    public PeepApiException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = null;
    }

    @Override
    public String getMessage() {
        if(message == null) {
            return errorCode.getMessage();
        }
        return String.format("%s", message);
    }

}
