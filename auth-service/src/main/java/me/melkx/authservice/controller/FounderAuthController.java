package me.melkx.authservice.controller;

import jakarta.validation.Valid;
import me.melkx.authmodule.dto.principal.CommonPrincipal;
import me.melkx.authmodule.strategy.jwt.dto.FounderAccessTokenPayload;
import me.melkx.authmodule.strategy.jwt.dto.FounderRefreshTokenPayload;
import me.melkx.authservice.dto.JwtTokenPairResponse;
import me.melkx.authservice.dto.SignInCredentialsRequest;
import me.melkx.authservice.dto.SignUpCredentialsRequest;
import me.melkx.authservice.service.FounderService;
import me.melkx.authservice.service.JwtRefreshService;
import me.melkx.jwtmodule.core.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/api/auth/founder")
public class FounderAuthController {
    private final FounderService founderService;
    private final JwtService jwtService;
    private final JwtRefreshService refreshService;

    @Autowired
    public FounderAuthController(FounderService founderService, JwtService jwtService, JwtRefreshService refreshService) {
        this.founderService = founderService;
        this.jwtService = jwtService;
        this.refreshService = refreshService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<JwtTokenPairResponse> signUp(@Valid @RequestBody SignUpCredentialsRequest credentials) {
        UUID id = founderService.createFounder(credentials);
        String refreshToken = jwtService.generateRefreshToken(new FounderRefreshTokenPayload(id));
        refreshService.registerRefreshToken(refreshToken);
        return ResponseEntity.ok(new JwtTokenPairResponse(
                jwtService.generateAccessToken(new FounderAccessTokenPayload(id)),
                refreshToken
        ));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtTokenPairResponse> signIn(@Valid @RequestBody SignInCredentialsRequest credentials) {
        String refreshToken = credentials.
        return new JwtTokenPairResponse(

        );
    }

    @PostMapping("/sign-out")
    public ResponseEntity<?> signOut(@AuthenticationPrincipal CommonPrincipal principal,
                                     @RequestParam(required = false) Boolean forcedExit) {
        return null;
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtTokenPairResponse> refreshToken(@AuthenticationPrincipal CommonPrincipal principal) {
        return null;
    }
}
