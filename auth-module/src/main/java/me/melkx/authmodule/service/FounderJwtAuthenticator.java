package me.melkx.authmodule.service;

import me.melkx.authmodule.dto.FounderPrincipal;
import me.melkx.authmodule.exception.UnauthorizedAuthenticationException;
import me.melkx.jwtmodule.core.api.dto.JwtTokenType;
import me.melkx.jwtmodule.core.api.service.FounderAccessTokenParser;
import me.melkx.jwtmodule.core.api.service.JwtParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class FounderJwtAuthenticator implements JwtAuthenticator {
    private final JwtParser jwtParser;

    public FounderJwtAuthenticator(JwtParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    @Override
    public Authentication authenticate(String token) {
        FounderAccessTokenParser parser = jwtParser.parseFounderAccessToken(token);
        if(parser.isCorrespondsType())
            throw new UnauthorizedAuthenticationException("Invalid token type");

        Authentication auth = new UsernamePasswordAuthenticationToken(new FounderPrincipal(parser.extractUUID()), null, null);
        auth.setAuthenticated(true);
        return auth;
    }
}
