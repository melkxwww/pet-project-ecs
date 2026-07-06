package me.melkx.jwtmodule.core.api.service;

public interface JwtService {
    String generateToken(TokenPayload payload);
    <T extends TokenPayload> T parseToken(String token, Class<T> targetClass);
}


