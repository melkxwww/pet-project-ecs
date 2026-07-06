package me.melkx.authmodule.dto.jwt;

import me.melkx.authmodule.dto.JwtTokenType;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public record FounderRefreshTokenPayload(UUID sub, UUID jti, Instant exp) implements TypedTokenPayload {
    public FounderRefreshTokenPayload(UUID sub) {
        this(sub, UUID.randomUUID(), null);
    }

    @Override
    public JwtTokenType getTokenType() {
        return JwtTokenType.FOUNDER_REFRESH;
    }
}
