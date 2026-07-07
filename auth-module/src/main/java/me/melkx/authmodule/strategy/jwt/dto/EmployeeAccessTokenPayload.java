package me.melkx.authmodule.strategy.jwt.dto;

import me.melkx.authmodule.dto.PrincipalType;
import me.melkx.jwtmodule.core.service.TokenType;

import java.util.UUID;

public record EmployeeAccessTokenPayload(UUID sub) implements TypedTokenPayload {

    @Override
    public PrincipalType getPrincipalType() {
        return PrincipalType.EMPLOYEE;
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.ACCESS;
    }
}
