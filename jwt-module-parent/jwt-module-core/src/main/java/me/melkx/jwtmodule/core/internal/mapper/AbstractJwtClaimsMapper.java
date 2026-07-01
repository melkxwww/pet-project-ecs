package me.melkx.jwtmodule.core.internal.mapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Objects;

public abstract class AbstractJwtClaimsMapper<T> implements JwtClaimsMapper<T> {
    @Override
    public final Claims mapTokenClaims(T claims) {
        Objects.requireNonNull(claims, "Token claims cannot be null");
        return mapTokenClaimsInternal(claims, Jwts.claims().build());
    }

    @Override
    public final T parseTokenClaims(Claims claims) {
        Objects.requireNonNull(claims, "Claims cannot be null");
        return parseTokenClaimsInternal(claims);
    }

    protected abstract Claims mapTokenClaimsInternal(T tokenClaims, Claims claims);

    protected abstract T parseTokenClaimsInternal(Claims claims);
}
