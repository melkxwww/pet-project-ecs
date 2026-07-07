package me.melkx.authmodule.strategy.jwt.service;

import me.melkx.authmodule.dto.PrincipalType;
import me.melkx.authmodule.dto.principal.FounderPrincipal;
import me.melkx.authmodule.strategy.jwt.dto.FounderAccessTokenPayload;
import me.melkx.jwtmodule.core.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

public class FounderPrincipalFactory implements JwtPrincipalFactory {
    private final JwtService jwtService;

    public FounderPrincipalFactory(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Object createPrincipal(String token) {
        FounderAccessTokenPayload payload = jwtService.parseToken(token, FounderAccessTokenPayload.class);
        return new UsernamePasswordAuthenticationToken(
                new FounderPrincipal(payload.sub()),
                null, null
        );
    }

    @Override
    public PrincipalType getType() {
        return PrincipalType.FOUNDER;
    }
}
