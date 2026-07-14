package me.melkx.authmodule.strategy.jwt.dto.employee;

import me.melkx.authmodule.strategy.jwt.dto.JsonPrincipalType;
import me.melkx.authmodule.strategy.jwt.dto.TypedTokenPayload;
import me.melkx.jwtmodule.core.dto.JsonTokenType;

import java.util.UUID;

public record EmployeeAccessTokenPayload(UUID sub) implements TypedTokenPayload {
    @Override
    public JsonPrincipalType getPrincipalType() {
        return JsonPrincipalType.EMPLOYEE;
    }

    @Override
    public JsonTokenType getTokenType() {
        return JsonTokenType.ACCESS;
    }
}
