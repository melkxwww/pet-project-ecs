package me.melkx.authservice.founder.service;

import me.melkx.authmodule.strategy.jwt.dto.founder.FounderAccessTokenPayload;
import me.melkx.authmodule.strategy.jwt.dto.founder.FounderRefreshTokenPayload;
import me.melkx.authservice.config.JwtValidityTimeProperties;
import me.melkx.authservice.dto.JwtTokenPairResponse;
import me.melkx.authservice.facade.JwtAuthFacade;
import me.melkx.authservice.founder.dto.FounderCredentialsRequest;
import me.melkx.authservice.founder.dto.FounderDto;
import me.melkx.authservice.founder.exception.FounderNotFoundException;
import me.melkx.jwtmodule.core.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class FounderAuthControllerFacade {
    private final FounderService founderService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthFacade authFacade;

    @Autowired
    public FounderAuthControllerFacade(FounderService founderService, PasswordEncoder passwordEncoder, JwtAuthFacade authFacade) {
        this.founderService = founderService;
        this.passwordEncoder = passwordEncoder;
        this.authFacade = authFacade;
    }

    public JwtTokenPairResponse signIn(FounderCredentialsRequest request) {
        FounderDto founder = founderService.findByEmail(request.email());
        if (!passwordEncoder.matches(request.password(), founder.passwordHash()))
            throw new FounderNotFoundException();

        String accessToken = authFacade.generateAccessToken(new FounderAccessTokenPayload(founder.uuid()));
        String refreshToken = authFacade.generateRefreshToken(new FounderRefreshTokenPayload(founder.uuid()));

        return new JwtTokenPairResponse(
                accessToken,
                refreshToken
        );
    }

    public JwtTokenPairResponse refresh(String refreshToken) {

    }
}
