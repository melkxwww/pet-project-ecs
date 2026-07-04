package me.melkx.authservice.controller;

import jakarta.validation.Valid;
import me.melkx.authenticationmodule.dto.CommonPrincipal;
import me.melkx.authservice.dto.JwtTokenPairResponse;
import me.melkx.authservice.dto.SignInCredentialsRequest;
import me.melkx.authservice.dto.SignUpCredentialsRequest;
import me.melkx.authservice.service.FounderService;
import me.melkx.jwtmodule.core.api.dto.FounderAccessTokenClaims;
import me.melkx.jwtmodule.core.api.dto.FounderRefreshTokenClaims;
import me.melkx.jwtmodule.core.api.dto.JwtTokenType;
import me.melkx.jwtmodule.core.api.service.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/api/auth/founder")
public class FounderAuthController {
    private final FounderService founderService;
    private final JwtGenerator jwtGenerator;

    @Autowired
    public FounderAuthController(FounderService founderService, JwtGenerator jwtGenerator) {
        this.founderService = founderService;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<JwtTokenPairResponse> signUp(@Valid @RequestBody SignUpCredentialsRequest credentials) {
        UUID id = founderService.createFounder(credentials);
        return ResponseEntity.ok(new JwtTokenPairResponse(
                jwtGenerator.generateFounderAccessToken(new FounderAccessTokenClaims(id)),
                jwtGenerator.generateFounderRefreshToken(new FounderRefreshTokenClaims(id))
        ));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtTokenPairResponse> signIn(@Valid @RequestBody SignInCredentialsRequest credentials) {
        return null;
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
