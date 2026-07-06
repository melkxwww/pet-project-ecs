package me.melkx.authmodule.service;

import org.springframework.security.core.Authentication;

import java.util.Objects;

public abstract class AbstractJwtAuthenticator implements JwtAuthenticator {
    protected final JwtServiceFacade jwtService;

    public AbstractJwtAuthenticator(JwtServiceFacade jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public final Authentication authenticate(String token) {
        Objects.requireNonNull(token, "Token must not be null");
        return authenticateInternal(token);
    }

    protected abstract Authentication authenticateInternal(String token);
}
