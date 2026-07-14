package me.melkx.authmodule.strategy.jwt.dto.founder;

import me.melkx.authmodule.strategy.jwt.dto.JsonPrincipalType;
import me.melkx.authmodule.strategy.jwt.dto.TypedTokenPayload;
import me.melkx.jwtmodule.core.dto.JsonTokenType;

import java.time.Instant;
import java.util.UUID;

public record FounderRefreshTokenPayload(UUID sub, UUID jti, Instant exp) implements TypedTokenPayload {
    public FounderRefreshTokenPayload(UUID sub) {
        this(sub, UUID.randomUUID(), null);
    }

    @Override
    public JsonPrincipalType getPrincipalType() {
        return JsonPrincipalType.FOUNDER;
    }

    @Override
    public JsonTokenType getTokenType() {
        return JsonTokenType.REFRESH;
    }
}
