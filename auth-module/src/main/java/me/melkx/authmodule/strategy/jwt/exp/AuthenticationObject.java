package me.melkx.authmodule.strategy.jwt.exp;


import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public record AuthenticationObject(Object principal, Set<? extends GrantedAuthority> authorities) {
}
