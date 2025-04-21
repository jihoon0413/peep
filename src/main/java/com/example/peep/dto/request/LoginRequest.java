package com.example.peep.dto.request;

import lombok.extern.java.Log;

public record LoginRequest(
        String userId,
        String password
) {
    public static LoginRequest of(String userId, String password) {
        return new LoginRequest(userId, password);
    }
}
