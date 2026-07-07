package me.melkx.authmodule.strategy.jwt.service;

import me.melkx.authmodule.dto.PrincipalType;
import me.melkx.authmodule.dto.principal.EmployeePrincipal;
import me.melkx.authmodule.strategy.jwt.dto.EmployeeAccessTokenPayload;
import me.melkx.jwtmodule.core.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

public class EmployeePrincipalFactory implements JwtPrincipalFactory {
    private final JwtService jwtService;

    public EmployeePrincipalFactory(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Object createPrincipal(String token) {
        EmployeeAccessTokenPayload payload = jwtService.parseToken(token, EmployeeAccessTokenPayload.class);
        return new UsernamePasswordAuthenticationToken(
                new EmployeePrincipal(payload.sub()),
                null, null
        );
    }

    @Override
    public PrincipalType getType() {
        return PrincipalType.EMPLOYEE;
    }
}
