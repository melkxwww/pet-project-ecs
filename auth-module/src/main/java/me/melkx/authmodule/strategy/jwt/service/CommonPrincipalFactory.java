package me.melkx.authmodule.strategy.jwt.service;

import me.melkx.authmodule.dto.PrincipalType;
import me.melkx.authmodule.dto.principal.CommonPrincipal;
import me.melkx.authmodule.strategy.jwt.dto.TypedTokenPayloadImpl;
import me.melkx.jwtmodule.core.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CommonPrincipalFactory implements JwtPrincipalFactory {
    private final JwtService jwtService;
    private final Map<PrincipalType, JwtPrincipalFactory> factoryMap;

    public CommonPrincipalFactory(JwtService jwtService, List<JwtPrincipalFactory> factories) {
        this.jwtService = jwtService;
        this.factoryMap = factories.stream()
                .filter(factory -> factory != this)
                .collect(Collectors.toMap(JwtPrincipalFactory::getType, f -> f));
    }

    @Override
    public Object createPrincipal(String token) {
        TypedTokenPayloadImpl payload = jwtService.parseToken(token, TypedTokenPayloadImpl.class);

        JwtPrincipalFactory factory = factoryMap.get(payload.getPrincipalType());
        if (factory == null) {
            throw new IllegalArgumentException("Unsupported principal type: " + payload.getPrincipalType());
        }
        return new CommonPrincipal(factory.getType(), factory.createPrincipal(token));
    }

    @Override
    public PrincipalType getType() {
        return PrincipalType.COMMON;
    }
}
