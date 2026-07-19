package me.melkx.authmodule.core.dto;

import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public record AuthenticationContext(Object principal, Set<? extends GrantedAuthority> authorities) {
}
