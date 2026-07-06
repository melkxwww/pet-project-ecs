package me.melkx.authmodule.dto.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import me.melkx.authmodule.dto.JwtTokenType;
import me.melkx.jwtmodule.core.service.TokenPayload;

public class UnsafeTypedTokenPayload implements TokenPayload {
    private final JwtTokenType jwtTokenType;

    public UnsafeTypedTokenPayload(JwtTokenType jwtTokenType) {
        this.jwtTokenType = jwtTokenType;
    }

    @JsonProperty
    public JwtTokenType getTokenType() {
        return jwtTokenType;
    }
}
