package me.melkx.jwtmodule.core.service;

import java.time.Duration;

public interface JwtService {
    String generateToken(TokenPayload payload, Duration validity);
    <T extends TokenPayload> T parseToken(String token, Class<T> targetClass);
}


