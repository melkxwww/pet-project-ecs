package me.melkx.authmodule.jwt.token;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.Set;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String token;
    private final Object principal;

    public JwtAuthenticationToken(String token) {
        super(Collections.emptySet());
        this.token = token;
        this.principal = null;
    }

    public JwtAuthenticationToken(Object principal, Set<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
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
