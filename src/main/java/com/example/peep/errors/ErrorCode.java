package com.example.peep.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token is invalid"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not founded"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Password is invalid"),
    ALREADY_EXECUTION(HttpStatus.CONFLICT,"User is already execution this request"),
    EMPTY_PARAMETER(HttpStatus.NOT_FOUND, "Empty parameter"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    FORBIDDEN(HttpStatus.FORBIDDEN,"Forbidden"),


    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Cannot find resource."),
    WRONG_VERIFICATION_CODE(HttpStatus.UNAUTHORIZED, "Wrong verificationCode");

    private final HttpStatus httpStatus;
    private final String message;

}

