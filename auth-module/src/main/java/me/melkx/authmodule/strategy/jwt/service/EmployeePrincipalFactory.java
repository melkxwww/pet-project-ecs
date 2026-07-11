package me.melkx.authmodule.strategy.jwt.service;

import me.melkx.authmodule.dto.principal.Principal;
import me.melkx.authmodule.dto.principal.EmployeePrincipal;
import me.melkx.authmodule.strategy.jwt.dto.EmployeeAccessTokenPayload;
import me.melkx.jwtmodule.core.service.JwtService;

public class EmployeePrincipalFactory implements JwtPrincipalFactory {
    private final JwtService jwtService;

    public EmployeePrincipalFactory(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Principal createPrincipal(String token) {
        EmployeeAccessTokenPayload payload = jwtService.parseToken(token, EmployeeAccessTokenPayload.class);
        return new EmployeePrincipal(payload.sub(), payload.companyId());
    }
}
