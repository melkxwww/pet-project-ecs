package me.melkx.authmodule.strategy.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.melkx.authmodule.exception.InternalAuthenticationException;
import me.melkx.authmodule.exception.UnauthorizedAuthenticationException;
import me.melkx.authmodule.dto.AuthenticationIgnoredUris;
import me.melkx.authmodule.service.AbstractAuthenticationFilter;
import me.melkx.authmodule.strategy.jwt.service.JwtAuthenticationFactory;
import me.melkx.jwtmodule.core.exception.JwtInvalidTokenException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends AbstractAuthenticationFilter {
    private final JwtAuthenticationFactory authenticationFactory;
    private final AuthenticationEntryPoint entryPoint;
    private final AuthenticationIgnoredUris ignoredUris;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return ignoredUris.getIgnoredUris().stream()
                .anyMatch(uri -> request.getRequestURI().startsWith(uri));
    }

    public JwtAuthenticationFilter(JwtAuthenticationFactory authenticationFactory, AuthenticationEntryPoint entryPoint, AuthenticationIgnoredUris ignoredUris) {
        this.authenticationFactory = authenticationFactory;
        this.entryPoint = entryPoint;
        this.ignoredUris = ignoredUris;
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

            SecurityContextHolder.getContext().setAuthentication(
                    authenticationFactory.createAuthentication(token)
            );

            filterChain.doFilter(request, response);
        } catch (JwtInvalidTokenException e) {
            SecurityContextHolder.clearContext();
            entryPoint.commence(request, response, new UnauthorizedAuthenticationException(e.getMessage()));
        } catch (UnauthorizedAuthenticationException e) {
            SecurityContextHolder.clearContext();
            entryPoint.commence(request, response, e);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            entryPoint.commence(request, response, new InternalAuthenticationException(e.getMessage()));
        }
    }
}