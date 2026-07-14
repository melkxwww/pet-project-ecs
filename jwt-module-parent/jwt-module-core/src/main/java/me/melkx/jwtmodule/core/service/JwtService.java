package me.melkx.jwtmodule.core.service;

import me.melkx.jwtmodule.core.dto.TokenPayload;

import java.time.Duration;

public interface JwtService {
    String generateToken(TokenPayload payload, Duration validity);
    <T extends TokenPayload> T parseToken(String token, Class<T> targetClass);
    <T extends TokenPayload> T readTokenClaims(String token, Class<T> targetClass);
}


