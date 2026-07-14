package me.melkx.authmodule.strategy.jwt.dto.employee;

import me.melkx.authmodule.strategy.jwt.dto.JsonPrincipalType;
import me.melkx.authmodule.strategy.jwt.dto.TypedTokenPayload;
import me.melkx.jwtmodule.core.dto.JsonTokenType;

import java.time.Instant;
import java.util.UUID;

public record EmployeeRefreshTokenPayload(UUID sub, UUID jti, Instant exp) implements TypedTokenPayload {
    public EmployeeRefreshTokenPayload(UUID sub) {
        this(sub, UUID.randomUUID(), null);
    }

    @Override
    public JsonPrincipalType getPrincipalType() {
        return JsonPrincipalType.EMPLOYEE;
    }

    @Override
    public JsonTokenType getTokenType() {
        return JsonTokenType.REFRESH;
    }
}
