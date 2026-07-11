package me.melkx.authmodule.strategy.jwt.dto;

import me.melkx.authmodule.dto.PrincipalType;
import me.melkx.jwtmodule.core.dto.TokenType;

import java.util.UUID;

public record FounderAccessTokenPayload(UUID sub) implements TypedTokenPayload {
    @Override
    public PrincipalType getPrincipalType() {
        return PrincipalType.FOUNDER;
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.ACCESS;
    }
}
