package me.melkx.authmodule.strategy.jwt.dto;

import me.melkx.authmodule.dto.PrincipalType;
import me.melkx.jwtmodule.core.dto.TokenType;

public class TypedTokenPayloadImpl implements TypedTokenPayload {
    private final PrincipalType principalType;
    private final TokenType tokenType;

    public TypedTokenPayloadImpl(PrincipalType principalType, TokenType tokenType) {
        this.principalType = principalType;
        this.tokenType = tokenType;
    }

    @Override
    public PrincipalType getPrincipalType() {
        return principalType;
    }

    @Override
    public TokenType getTokenType() {
        return tokenType;
    }
}
