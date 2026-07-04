package me.melkx.authmodule.service;

import me.melkx.jwtmodule.core.api.dto.JwtTokenType;
import me.melkx.jwtmodule.core.api.service.JwtParser;
import org.springframework.security.core.Authentication;

import java.util.Objects;

public abstract class AbstractJwtAuthenticator implements JwtAuthenticator {
    protected final JwtParser jwtParser;

    public AbstractJwtAuthenticator(JwtParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    @Override
    public Authentication authenticate(String token) {
        Objects.requireNonNull(token, "Token must not be null");
        return authenticateInternal(jwtParser.parseJwtTokenType(token), token);
    }

    protected abstract Authentication authenticateInternal(JwtTokenType tokenType, String token);
}
