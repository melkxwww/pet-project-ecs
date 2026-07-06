package me.melkx.authservice.service;

import me.melkx.authmodule.dto.JwtTokenType;
import me.melkx.jwtmodule.core.service.TokenPayload;

import java.util.UUID;

public record CommonRefreshTokenPayload(UUID sub, String jti, JwtTokenType tokenType) implements TokenPayload {
}
