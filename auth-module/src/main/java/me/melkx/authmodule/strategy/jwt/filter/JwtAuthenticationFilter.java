package me.melkx.authmodule.strategy.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.melkx.authmodule.dto.principal.Principal;
import me.melkx.authmodule.exception.InternalAuthenticationException;
import me.melkx.authmodule.exception.UnauthorizedAuthenticationException;
import me.melkx.authmodule.dto.IgnoredUris;
import me.melkx.authmodule.service.PrincipalAuthoritiesProvider;
import me.melkx.authmodule.strategy.jwt.service.JwtPrincipalFactory;
import me.melkx.jwtmodule.core.exception.JwtInternalException;
import me.melkx.jwtmodule.core.exception.JwtInvalidTokenException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtPrincipalFactory principalFactory;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final IgnoredUris ignoredUris;

    private final Optional<PrincipalAuthoritiesProvider> authoritiesProvider;

    public JwtAuthenticationFilter(JwtPrincipalFactory principalFactory, AuthenticationEntryPoint authenticationEntryPoint, IgnoredUris ignoredUris, Optional<PrincipalAuthoritiesProvider> authoritiesProvider) {
        this.principalFactory = principalFactory;
        this.authoritiesProvider = authoritiesProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.ignoredUris = ignoredUris;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return ignoredUris.getIgnoredUris().stream()
                .anyMatch(uri -> request.getRequestURI().startsWith(uri));
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

            Principal principal = principalFactory.createPrincipal(token);
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    authoritiesProvider.orElse(null).provide()
            );
            auth.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
        } catch (JwtInternalException e) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, new InternalAuthenticationException("Authentication service error"));
        } catch (JwtInvalidTokenException e) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, new UnauthorizedAuthenticationException("Invalid authentication token"));
        } catch (UnauthorizedAuthenticationException e) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, e);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response, new InternalAuthenticationException("Authentication failed"));
        }
    }
}