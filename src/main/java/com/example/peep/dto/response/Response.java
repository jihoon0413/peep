package com.example.peep.dto.response;

import com.example.peep.errors.ErrorCode;

public record Response<T>(
        String resultCode,
        T result
) {

    public static Response<Void> success() {
        return new Response<Void>("SUCCESS", null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<T>("SUCCESS", result);
    }

    public static Response<String> error(ErrorCode errorCode, String message) {
        String msg = message;
        if(msg == null) {
            msg = errorCode.getMessage();
        }
        return new Response<String>(errorCode.name(), msg);
    }

    public String toStream() {
        if(result == null) {
            return "{" +
                    "\"resultCode\":" + "\"" + resultCode + "\"," +
                    "\"result\":" + null + "}";
        }

        return "{" +
                "\"resultCode\":" + "\"" + resultCode + "\"," +
                "\"result\":" + "\"" + result + "\"" + "}";
    }

}
