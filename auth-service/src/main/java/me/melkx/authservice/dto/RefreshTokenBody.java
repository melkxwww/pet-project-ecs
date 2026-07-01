package me.melkx.authservice.dto;

import java.util.Objects;
import java.util.UUID;

public record RefreshTokenBody(String refreshToken, UUID jti) {
    public RefreshTokenBody {
        Objects.requireNonNull(refreshToken, "Refresh token cannot be null");
        Objects.requireNonNull(jti, "JTI cannot be null");
    }
}
