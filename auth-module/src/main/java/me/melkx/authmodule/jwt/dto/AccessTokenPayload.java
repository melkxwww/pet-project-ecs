package me.melkx.authmodule.jwt.dto;

import me.melkx.jwtmodule.dto.JsonTokenType;
import me.melkx.jwtmodule.dto.TokenPayload;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.UUID;

public record AccessTokenPayload(UUID sub) implements TokenPayload {
    public AccessTokenPayload {
        Objects.requireNonNull(sub, "sub cannot be null");
    }

    @Override
    public JsonTokenType getTokenType() {
        return JsonTokenType.ACCESS;
    }
}
