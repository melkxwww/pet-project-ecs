package me.melkx.authservice.facade;

import me.melkx.authmodule.strategy.jwt.dto.TypedTokenPayload;
import me.melkx.authservice.config.JwtValidityTimeProperties;
import me.melkx.jwtmodule.core.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class JwtAuthFacade {
    private final JwtService jwtService;
    private final JwtValidityTimeProperties jwtTimeProperties;

    @Autowired
    public JwtAuthFacade(JwtService jwtService, JwtValidityTimeProperties jwtTimeProperties) {
        this.jwtService = jwtService;
        this.jwtTimeProperties = jwtTimeProperties;
    }

    public String generateAccessToken(TypedTokenPayload payload) {
        return jwtService.generateToken(payload, Duration.ofSeconds(jwtTimeProperties.getAccessValiditySeconds()));
    }

    public String generateRefreshToken(TypedTokenPayload payload) {
        return jwtService.generateToken(payload, Duration.ofSeconds(jwtTimeProperties.getRefreshValiditySeconds()));
    }
}
