package com.example.peep.dto.request;

public record VerifyCodeRequestDto(
        String phoneNumber,
        String verifyCode
) {
    public static VerifyCodeRequestDto of(String phoneNumber, String verifyCode) {
        return new VerifyCodeRequestDto(phoneNumber, verifyCode);
    }
}
