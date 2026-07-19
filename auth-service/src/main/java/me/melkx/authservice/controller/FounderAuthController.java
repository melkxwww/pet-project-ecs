package me.melkx.authservice.controller;

import jakarta.validation.Valid;
import me.melkx.authservice.dto.FounderAuthenticationRequest;
import me.melkx.authservice.dto.JwtTokenPairResponse;
import me.melkx.jwtmodule.core.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/auth/founders")
public class FounderAuthController {
    private final JwtService jwtService;

    @Autowired
    public FounderAuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/sign-in")
    public ResponseEntity<JwtTokenPairResponse> signIn(@RequestBody @Valid FounderAuthenticationRequest request) {
        // MATCH PASSWORD
        // BUILD JWT TOKEN
        // RETURN
        return null;
    }

    @GetMapping("/sign-out")
    public void signOut(boolean forceExit) {

    }

    @GetMapping("/refresh")
    public void refresh() {
    }
}
