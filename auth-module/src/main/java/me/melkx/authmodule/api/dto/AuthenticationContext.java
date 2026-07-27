package me.melkx.authmodule.api.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;
import java.util.Set;

public record AuthenticationContext(Object principal, Set<? extends GrantedAuthority> authorities) {
    public AuthenticationContext {
        Objects.requireNonNull(principal, "principal cannot be null");
        Objects.requireNonNull(authorities, "authorities cannot be null");
    }
}
