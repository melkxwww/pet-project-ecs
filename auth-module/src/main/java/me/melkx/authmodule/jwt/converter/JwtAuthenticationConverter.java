package me.melkx.authmodule.jwt.converter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.melkx.authmodule.jwt.token.JwtAuthenticationToken;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

@Slf4j
public class JwtAuthenticationConverter implements AuthenticationConverter {
    @Override
    public @Nullable Authentication convert(HttpServletRequest request) {
        log.debug("Converting request to authentication...");

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

        log.debug("Converting to authentication successful!");

        return new JwtAuthenticationToken(token);
    }
}
