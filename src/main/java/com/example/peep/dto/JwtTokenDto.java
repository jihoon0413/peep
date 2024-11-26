package com.example.peep.dto;

import com.example.peep.config.jwt.JwtToken;

public record JwtTokenDto(
        String grantType,
        String accessToken,
        String refreshToken,
        String id
) {
    public static JwtTokenDto of(String grantType, String accessToken, String refreshToken, String userId) {
        return new JwtTokenDto(grantType, accessToken, refreshToken, userId);
    }

    public static JwtTokenDto from(JwtToken jwtToken) {
        return new JwtTokenDto(
                jwtToken.getGrantType(),
                jwtToken.getAccessToken(),
                jwtToken.getRefreshToken(),
                jwtToken.getId()
        );
    }

    public JwtToken toEntity() {
        return JwtToken.of(
                grantType,
                accessToken,
                refreshToken,
                id
        );
    }

}

