package me.melkx.authmodule.strategy.jwt.service;

import me.melkx.authmodule.dto.principal.Principal;
import me.melkx.authmodule.dto.principal.FounderPrincipal;
import me.melkx.authmodule.strategy.jwt.dto.FounderAccessTokenPayload;
import me.melkx.jwtmodule.core.service.JwtService;

public class FounderPrincipalFactory implements JwtPrincipalFactory {
    private final JwtService jwtService;

    public FounderPrincipalFactory(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Principal createPrincipal(String token) {
        FounderAccessTokenPayload payload = jwtService.parseToken(token, FounderAccessTokenPayload.class);
        return new FounderPrincipal(payload.sub());
    }
}
