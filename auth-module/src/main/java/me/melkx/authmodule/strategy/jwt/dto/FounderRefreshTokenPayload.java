package me.melkx.authmodule.strategy.jwt.dto;

import me.melkx.authmodule.dto.PrincipalType;
import me.melkx.jwtmodule.core.dto.TokenType;

import java.time.Instant;
import java.util.UUID;

public record FounderRefreshTokenPayload(UUID sub, UUID jti, Instant exp) implements TypedTokenPayload {
    public FounderRefreshTokenPayload(UUID sub) {
        this(sub, UUID.randomUUID(), null);
    }

    @Override
    public PrincipalType getPrincipalType() {
        return PrincipalType.FOUNDER;
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.REFRESH;
    }
}
