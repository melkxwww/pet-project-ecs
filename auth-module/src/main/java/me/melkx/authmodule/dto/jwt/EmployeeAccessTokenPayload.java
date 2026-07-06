package me.melkx.authmodule.dto.jwt;

import me.melkx.authmodule.dto.JwtTokenType;

import java.util.UUID;

public record EmployeeAccessTokenPayload(UUID sub) implements TypedTokenPayload {
    @Override
    public JwtTokenType getUserType() {
        return JwtTokenType.EMPLOYEE_ACCESS;
    }
}
