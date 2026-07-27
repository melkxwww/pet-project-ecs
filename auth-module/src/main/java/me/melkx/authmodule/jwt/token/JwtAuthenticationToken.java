package me.melkx.authmodule.jwt.token;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String token;
    private final Object principal;

    public JwtAuthenticationToken(String token) {
        super(Collections.emptySet());
        this.token = Objects.requireNonNull(token, "token cannot be null");
        this.principal = null;
    }

    public JwtAuthenticationToken(Object principal, Set<? extends GrantedAuthority> authorities) {
        super(Objects.requireNonNull(authorities, "authorities cannot be null"));
        this.principal = Objects.requireNonNull(principal, "principal cannot be null");
        this.token = null;
    }

    @Override
    public @Nullable Object getCredentials() {
        return null;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return token == null ? principal : token;
    }
}
