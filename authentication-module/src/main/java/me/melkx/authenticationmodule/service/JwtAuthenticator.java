package me.melkx.authenticationmodule.service;

import org.springframework.security.core.Authentication;

public interface JwtAuthenticator {
    Authentication authenticate(String token);
}
