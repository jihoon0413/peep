package com.example.peep.config.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String id;

    public static JwtToken of(String grantType, String accessToken, String refreshToken, String id) {
        return new JwtToken(grantType, accessToken, refreshToken, id);
    }
}