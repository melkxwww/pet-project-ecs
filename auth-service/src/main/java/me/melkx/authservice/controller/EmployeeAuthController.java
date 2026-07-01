package me.melkx.authservice.controller;

import jakarta.validation.Valid;
import me.melkx.authenticationmodule.dto.CommonPrincipal;
import me.melkx.authservice.dto.JwtTokenPairResponse;
import me.melkx.authservice.dto.SignInCredentialsRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/employee")
public class EmployeeAuthController {
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
