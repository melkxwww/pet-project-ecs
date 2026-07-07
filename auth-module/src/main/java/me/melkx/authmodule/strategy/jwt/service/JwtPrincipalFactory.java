package me.melkx.authmodule.strategy.jwt.service;

import me.melkx.authmodule.dto.PrincipalType;

public interface JwtPrincipalFactory {
    Object createPrincipal(String token);
    PrincipalType getType();
}
