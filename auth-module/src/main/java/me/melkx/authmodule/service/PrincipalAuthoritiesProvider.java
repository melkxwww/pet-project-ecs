package me.melkx.authmodule.service;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface PrincipalAuthoritiesProvider {
    Collection<? extends GrantedAuthority> provide(String subject);
}
