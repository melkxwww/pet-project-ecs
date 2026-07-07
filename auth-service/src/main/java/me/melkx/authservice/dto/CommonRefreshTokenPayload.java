package me.melkx.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.melkx.authmodule.dto.PrincipalType;
import me.melkx.authmodule.strategy.jwt.dto.TypedTokenPayload;
import me.melkx.jwtmodule.core.service.TokenPayload;
import me.melkx.jwtmodule.core.service.TokenType;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CommonRefreshTokenPayload implements TypedTokenPayload {
    private final UUID sub;
    private final UUID jti;
    private final Instant exp;
    private final PrincipalType principalType;
    private final TokenType tokenType;

    @Override
    public PrincipalType getPrincipalType() {
        return principalType;
    }

    @Override
    public TokenType getTokenType() {
        return tokenType;
    }
}
