package com.example.peep.dto.response;

import com.example.peep.config.jwt.JwtToken;

public record JwtTokenResponse(
        String grantType,
        String accessToken,
        String refreshToken,
        String id
) {
    public static JwtTokenResponse of(String grantType, String accessToken, String refreshToken, String userId) {
        return new JwtTokenResponse(grantType, accessToken, refreshToken, userId);
    }

    public static JwtTokenResponse from(JwtToken jwtToken) {
        return new JwtTokenResponse(
                jwtToken.getGrantType(),
                jwtToken.getAccessToken(),
                jwtToken.getRefreshToken(),
                jwtToken.getId()
        );
    }

}

