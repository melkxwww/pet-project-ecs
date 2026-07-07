package me.melkx.jwtmodule.core.service;

import java.time.Duration;

public interface JwtService {
    String generateAccessToken(TokenPayload payload);
    String generateRefreshToken(TokenPayload payload);
    <T extends TokenPayload> T parseToken(String token, Class<T> targetClass);
}


