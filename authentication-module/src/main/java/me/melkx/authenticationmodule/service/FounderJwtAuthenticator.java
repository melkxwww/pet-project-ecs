package me.melkx.authenticationmodule.service;

import me.melkx.authenticationmodule.dto.CommonPrincipal;
import me.melkx.authenticationmodule.dto.FounderPrincipal;
import me.melkx.authenticationmodule.dto.PrincipalType;
import me.melkx.authenticationmodule.exception.UnauthorizedAuthenticationException;
import me.melkx.jwtmodule.core.api.dto.FounderAccessTokenClaims;
import me.melkx.jwtmodule.core.api.dto.JwtTokenType;
import me.melkx.jwtmodule.core.api.service.JwtParser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.UUID;

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
