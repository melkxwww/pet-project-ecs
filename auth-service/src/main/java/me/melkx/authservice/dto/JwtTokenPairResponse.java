package me.melkx.authservice.dto;

import java.util.Objects;

public record JwtTokenPairResponse(String accessToken, String refreshToken) {
    public JwtTokenPairResponse {
        Objects.requireNonNull(accessToken, "Access token cannot be null");
        Objects.requireNonNull(refreshToken, "Refresh token cannot be null");
    }
}
