package me.melkx.authenticationmodule.service;

import me.melkx.jwtmodule.core.api.dto.JwtTokenType;
import me.melkx.jwtmodule.core.api.service.JwtParser;
import org.springframework.security.core.Authentication;

public class EmployeeJwtAuthenticator extends AbstractJwtAuthenticator {
    public EmployeeJwtAuthenticator(JwtParser jwtParser) {
        super(jwtParser);
    }

    @Override
    protected Authentication authenticateInternal(JwtTokenType tokenType, String token) {
        return null;
    }
}
