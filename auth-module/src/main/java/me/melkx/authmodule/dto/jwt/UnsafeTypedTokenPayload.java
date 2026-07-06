package me.melkx.authmodule.dto.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import me.melkx.authmodule.dto.JwtTokenType;
import me.melkx.jwtmodule.core.service.TokenPayload;

public record UnsafeTypedTokenPayload(JwtTokenType tokenType) implements TokenPayload {
}
