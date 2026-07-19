package me.melkx.authmodule.jwt.converter;

import jakarta.servlet.http.HttpServletRequest;
import me.melkx.authmodule.jwt.token.JwtAuthenticationToken;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

public class JwtAuthenticationConverter implements AuthenticationConverter {
    @Override
    public @Nullable Authentication convert(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || authHeader.isBlank()) {
            throw new BadCredentialsException("Authorization header is missing");
        }

        if (!authHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("Authorization header must start with 'Bearer '");
        }

        String token = authHeader.substring(7);
        if (token.isBlank()) {
            throw new BadCredentialsException("Authorization token is empty");
        }

        return new JwtAuthenticationToken(token);
    }
}
