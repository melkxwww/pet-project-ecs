package me.melkx.authmodule.strategy.jwt.service;

import org.springframework.security.core.Authentication;

public interface JwtAuthenticationFactory {
    Authentication createAuthentication(String token);
}
