package com.example.peep.errors.exception;

import com.example.peep.errors.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException{

    private final ErrorCode errorCode;

}
