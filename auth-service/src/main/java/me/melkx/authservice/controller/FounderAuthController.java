package me.melkx.authservice.controller;

import jakarta.validation.Valid;
import me.melkx.authenticationmodule.dto.CommonPrincipal;
import me.melkx.authservice.dto.JwtTokenPairResponse;
import me.melkx.authservice.dto.SignInCredentialsRequest;
import me.melkx.authservice.dto.SignUpCredentialsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/founder")
public class FounderAuthController {
    @PostMapping("/sign-up")
    public ResponseEntity<JwtTokenPairResponse> signUp(@Valid @RequestBody SignUpCredentialsRequest credentials) {
        return null;
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
