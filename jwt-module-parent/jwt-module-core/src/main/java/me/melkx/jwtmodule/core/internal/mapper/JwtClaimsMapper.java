package me.melkx.jwtmodule.core.internal.mapper;

import io.jsonwebtoken.Claims;

public interface JwtClaimsMapper<T> {
    Claims mapTokenClaims(T claims);
    T parseTokenClaims(Claims claims);
}
