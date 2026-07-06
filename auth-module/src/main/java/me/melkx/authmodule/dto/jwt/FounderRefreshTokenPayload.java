package me.melkx.authmodule.dto.jwt;

import me.melkx.authmodule.dto.JwtTokenType;

import java.util.UUID;

public record FounderRefreshTokenPayload(UUID sub, UUID jti) implements TypedTokenPayload {
    public FounderRefreshTokenPayload(UUID sub) {
        this(sub, UUID.randomUUID());
    }

    @Override
    public JwtTokenType getUserType() {
        return JwtTokenType.FOUNDER_REFRESH;
    }
}
