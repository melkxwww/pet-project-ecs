package me.melkx.authmodule.service;

import me.melkx.authmodule.dto.PrincipalType;
import me.melkx.authmodule.dto.jwt.EmployeeAccessTokenPayload;
import me.melkx.authmodule.dto.jwt.FounderAccessTokenPayload;
import me.melkx.authmodule.dto.jwt.UnsafeTypedTokenPayload;
import me.melkx.authmodule.dto.principal.CommonPrincipal;
import me.melkx.authmodule.dto.principal.EmployeePrincipal;
import me.melkx.authmodule.dto.principal.FounderPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class CommonJwtAuthenticator extends AbstractJwtAuthenticator {
    public CommonJwtAuthenticator(JwtServiceFacade jwtService) {
        super(jwtService);
    }

    @Override
    protected Authentication authenticateInternal(String token) {
        UnsafeTypedTokenPayload payload = jwtService.parseToken(token, UnsafeTypedTokenPayload.class);

        Object principal = switch (payload.getTokenType()) {
            case FOUNDER_ACCESS ->
                    new FounderPrincipal(jwtService.parseToken(token, FounderAccessTokenPayload.class).sub());

            case EMPLOYEE_ACCESS ->
                    new EmployeePrincipal(jwtService.parseToken(token, EmployeeAccessTokenPayload.class).sub());

            default ->
                    throw new IllegalArgumentException("Invalid token type: " + payload.getTokenType());
        };

        PrincipalType principalType = PrincipalType.fromJwtTokenType(payload.getTokenType());
        CommonPrincipal commonPrincipal = new CommonPrincipal(principalType, principal);

        return new UsernamePasswordAuthenticationToken(commonPrincipal, null, null);
    }
}
