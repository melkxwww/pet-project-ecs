package me.melkx.authmodule.dto.jwt;

import me.melkx.authmodule.dto.JwtTokenType;

import java.util.UUID;

public record FounderAccessTokenPayload(UUID sub) implements TypedTokenPayload {
    @Override
    public JwtTokenType getTokenType() {
        return JwtTokenType.FOUNDER_ACCESS;
    }
}
