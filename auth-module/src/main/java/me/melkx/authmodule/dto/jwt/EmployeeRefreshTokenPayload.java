package me.melkx.authmodule.dto.jwt;

import me.melkx.authmodule.dto.JwtTokenType;

import java.time.Instant;
import java.util.UUID;

public record EmployeeRefreshTokenPayload(UUID sub, UUID jti, Instant exp) implements TypedTokenPayload {
    public EmployeeRefreshTokenPayload(UUID sub) {
        this(sub, UUID.randomUUID(), null);
    }

    @Override
    public JwtTokenType getTokenType() {
        return JwtTokenType.EMPLOYEE_REFRESH;
    }
}
