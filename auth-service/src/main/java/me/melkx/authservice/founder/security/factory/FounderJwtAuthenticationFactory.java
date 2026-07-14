package me.melkx.authservice.founder.security.factory;

import me.melkx.authmodule.strategy.jwt.dto.FounderAccessTokenPayload;
import me.melkx.authmodule.strategy.jwt.service.AbstractJwtAuthenticationFactory;
import me.melkx.authservice.founder.security.dto.FounderPrincipal;
import me.melkx.jwtmodule.core.service.JwtService;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

public class FounderJwtAuthenticationFactory extends AbstractJwtAuthenticationFactory<FounderPrincipal> {
    private final JwtService jwtService;

    public FounderJwtAuthenticationFactory(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected FounderPrincipal createPrincipalByToken(String s) {
        FounderAccessTokenPayload payload = jwtService.parseToken(s, FounderAccessTokenPayload.class);
        return new FounderPrincipal(payload.sub());
    }

    @Override
    protected Set<? extends GrantedAuthority> getAuthoritiesByPrincipal(FounderPrincipal founderPrincipal) {
        return Set.of();
    }
}
