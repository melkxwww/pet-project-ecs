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

public class CommonJwtAuthenticator extends AbstractJwtAuthenticator {
    public CommonJwtAuthenticator(JwtParser jwtParser) {
        super(jwtParser);
    }

    @Override
    protected Authentication authenticateInternal(JwtTokenType tokenType, String token) {
        Authentication auth;
        switch (tokenType) {
            case FOUNDER_ACCESS_TOKEN:
                FounderAccessTokenClaims claims = jwtParser.parseFounderAccessToken(token);
                auth = new UsernamePasswordAuthenticationToken(
                        new CommonPrincipal(PrincipalType.FOUNDER, new FounderPrincipal(claims.id())), null, null
                );
                break;
            //TODO: create employee claims
            default:
                throw new UnauthorizedAuthenticationException("Invalid token type");
        }
        auth.setAuthenticated(true);
        return auth;
    }
}
