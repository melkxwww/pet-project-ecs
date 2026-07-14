package me.melkx.authmodule.strategy.jwt.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;
import java.util.Set;

public abstract class AbstractJwtAuthenticationFactory<T> implements JwtAuthenticationFactory {
    @Override
    public Authentication createAuthentication(String token) {
        Objects.requireNonNull(token, "Token cannot be null");

        T principal = createPrincipalByToken(token);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                getAuthoritiesByPrincipal(principal)
        );
        authentication.setAuthenticated(true);
        return authentication;
    }

    protected abstract T createPrincipalByToken(String token);

    protected abstract Set<? extends GrantedAuthority> getAuthoritiesByPrincipal(T principal);
}
