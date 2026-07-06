package me.melkx.authmodule.service;

import me.melkx.jwtmodule.core.service.JwtService;
import me.melkx.jwtmodule.core.service.TokenPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class JwtServiceFacade {

    private final JwtService jwtService;
    private final long accessValidity;
    private final long refreshValidity;

    public JwtServiceFacade(
            JwtService jwtService,
            @Value("${app.jwt.access-validity-seconds}") long accessValidity,
            @Value("${app.jwt.refresh-validity-seconds}") long refreshValidity
    ) {
        this.jwtService = jwtService;
        this.accessValidity = accessValidity;
        this.refreshValidity = refreshValidity;
    }

    public String generateAccessToken(TokenPayload tokenPayload) {
        return jwtService.generateToken(tokenPayload, Duration.ofSeconds(accessValidity));
    }

    public String generateRefreshToken(TokenPayload tokenPayload) {
        return jwtService.generateToken(tokenPayload, Duration.ofSeconds(refreshValidity));
    }

    public <T extends TokenPayload> T parseToken(String token, Class<T> clazz) {
        return jwtService.parseToken(token, clazz);
    }
}
