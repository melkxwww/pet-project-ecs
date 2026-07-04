package me.melkx.authmodule.service;

import org.springframework.security.core.Authentication;

public interface JwtAuthenticator {
    Authentication authenticate(String token);
}
