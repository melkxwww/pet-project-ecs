package me.melkx.authmodule.jwt.dto;

import me.melkx.jwtmodule.core.dto.JsonTokenType;
import me.melkx.jwtmodule.core.dto.TokenPayload;

import java.util.UUID;

public record AccessTokenPayload(UUID id) implements TokenPayload {
    @Override
    public JsonTokenType getTokenType() {
        return JsonTokenType.ACCESS;
    }
}
