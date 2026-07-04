package me.melkx.authmodule.service;

import me.melkx.authmodule.dto.FounderPrincipal;
import me.melkx.authmodule.exception.UnauthorizedAuthenticationException;
import me.melkx.jwtmodule.core.api.dto.FounderAccessTokenClaims;
import me.melkx.jwtmodule.core.api.dto.JwtTokenType;
import me.melkx.jwtmodule.core.api.service.JwtParser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class FounderJwtAuthenticator extends  AbstractJwtAuthenticator {
    public FounderJwtAuthenticator(JwtParser jwtParser) {
        super(jwtParser);
    }

    @Override
    protected Authentication authenticateInternal(JwtTokenType tokenType, String token) {
        if(tokenType != JwtTokenType.FOUNDER_ACCESS_TOKEN)
            throw new UnauthorizedAuthenticationException("Invalid token type");

        FounderAccessTokenClaims claims = jwtParser.parseFounderAccessToken(token);
        Authentication auth = new UsernamePasswordAuthenticationToken(new FounderPrincipal(claims.id()), null, null);
        auth.setAuthenticated(true);
        return auth;
    }
}
