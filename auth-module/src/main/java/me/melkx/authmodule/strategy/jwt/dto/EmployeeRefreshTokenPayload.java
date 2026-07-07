package me.melkx.authmodule.strategy.jwt.dto;

import me.melkx.authmodule.dto.PrincipalType;
import me.melkx.jwtmodule.core.service.TokenType;

import java.time.Instant;
import java.util.UUID;

public record EmployeeRefreshTokenPayload(UUID sub, UUID jti, Instant exp) implements TypedTokenPayload {
    public EmployeeRefreshTokenPayload(UUID sub) {
        this(sub, UUID.randomUUID(), null);
    }

    @Override
    public PrincipalType getPrincipalType() {
        return PrincipalType.EMPLOYEE;
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.REFRESH;
    }
}
