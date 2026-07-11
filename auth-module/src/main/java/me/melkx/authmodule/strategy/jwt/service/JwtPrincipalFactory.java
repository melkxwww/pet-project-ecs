package me.melkx.authmodule.strategy.jwt.service;

import me.melkx.authmodule.dto.principal.Principal;

public interface JwtPrincipalFactory {
    Principal createPrincipal(String token);
}
