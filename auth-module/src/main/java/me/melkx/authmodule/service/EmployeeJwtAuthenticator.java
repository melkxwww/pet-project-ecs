package me.melkx.authmodule.service;

import me.melkx.authmodule.dto.jwt.EmployeeAccessTokenPayload;
import me.melkx.authmodule.dto.jwt.FounderAccessTokenPayload;
import me.melkx.authmodule.dto.principal.EmployeePrincipal;
import me.melkx.authmodule.dto.principal.FounderPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class EmployeeJwtAuthenticator extends AbstractJwtAuthenticator {
    public EmployeeJwtAuthenticator(JwtServiceFacade jwtService) {
        super(jwtService);
    }

    @Override
    protected Authentication authenticateInternal(String token) {
        EmployeeAccessTokenPayload payload = jwtService.parseToken(token, EmployeeAccessTokenPayload.class);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                new EmployeePrincipal(payload.sub()),
                null,
                null
        );
        auth.setAuthenticated(true);
        return auth;
    }
}
