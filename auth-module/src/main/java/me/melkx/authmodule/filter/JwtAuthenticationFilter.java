package me.melkx.authmodule.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.melkx.authmodule.exception.InternalAuthenticationException;
import me.melkx.authmodule.exception.UnauthorizedAuthenticationException;
import me.melkx.authmodule.dto.IgnoredUris;
import me.melkx.authmodule.service.JwtAuthenticator;
import me.melkx.jwtmodule.core.api.exception.JwtInvalidTokenException;
import me.melkx.jwtmodule.core.api.exception.JwtInternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtAuthenticator authenticator;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final IgnoredUris ignoredUris;

    @Autowired
    public JwtAuthenticationFilter(JwtAuthenticator authenticator, AuthenticationEntryPoint authenticationEntryPoint, IgnoredUris ignoredUris) {
        this.authenticator = authenticator;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.ignoredUris = ignoredUris;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return ignoredUris.getUris().stream().anyMatch(uri -> request.getRequestURI().startsWith(uri));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || authHeader.isBlank()) {
                throw new UnauthorizedAuthenticationException("Authorization header is missing");
            }

            if (!authHeader.startsWith("Bearer ")) {
                throw new UnauthorizedAuthenticationException("Authorization header must start with 'Bearer '");
            }

            String token = authHeader.substring(7);
            if (token.isBlank()) {
                throw new UnauthorizedAuthenticationException("Authorization token is empty");
            }

            log.debug("Authenticating request: {}", request.getRequestURI());

            var authentication = authenticator.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.debug("Authentication successful for: {}", authentication.getName());

            filterChain.doFilter(request, response);
        } catch (JwtInternalException e) {
            log.error("JWT internal error", e);

            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, new InternalAuthenticationException("Authentication service error"));
        } catch (JwtInvalidTokenException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());

            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, new UnauthorizedAuthenticationException("Invalid authentication token"));
        } catch (UnauthorizedAuthenticationException e) {
            log.warn("Authentication failed: {}", e.getMessage());

            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, e);
        } catch (Exception e) {
            log.error("Unexpected error in JWT filter", e);

            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, new InternalAuthenticationException("Authentication failed"));
        }
    }
}