package me.melkx.authservice.security.token;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class SignInAuthenticationToken extends AbstractAuthenticationToken {
    private final Object id;
    private final String password;
    private final Object principal;

    public SignInAuthenticationToken(Object id, String password) {
        super(Collections.emptySet());
        this.id = id;
        this.password = password;
        this.principal = null;
    }

    public SignInAuthenticationToken(Object principal) {
        super(Collections.emptySet());
        this.principal = principal;
        this.id = null;
        this.password = null;
    }

    @Override
    public @Nullable Object getCredentials() {
        return password;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return id == null ? principal : id;
    }
}
