package me.melkx.authmodule.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * Used for internal authentication implementations of the current module and for Auth Service.
 * Use in other classes is undesirable
 */
@Slf4j
public class DelegatingAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final AuthenticationConverter authenticationConverter;
    private final AuthenticationEntryPoint entryPoint;
    private final @Nullable RequestMatcher skipMatcher;

    public DelegatingAuthenticationFilter(AuthenticationManager authenticationManager,
                                          AuthenticationConverter authenticationConverter,
                                          AuthenticationEntryPoint entryPoint,
                                          @Nullable RequestMatcher skipMatcher) {
        this.authenticationManager = Objects.requireNonNull(authenticationManager, "authenticationManager cannot be null");
        this.authenticationConverter = Objects.requireNonNull(authenticationConverter, "authenticationConverter cannot be null");
        this.entryPoint = Objects.requireNonNull(entryPoint, "entryPoint cannot be null");
        this.skipMatcher = skipMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("Delegating authentication filter started...");

        try {
            Authentication authRequest = authenticationConverter.convert(request);

            if (authRequest == null) {
                logger.warn("authRequest is null");

                filterChain.doFilter(request, response);
                return;
            }

            logger.debug("authRequest correct; Authenticating...");

            Authentication authResult = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authResult);
        } catch (AuthenticationException e) {
            logger.warn("Failed to authenticate (formatted exception): " + e.getMessage());

            SecurityContextHolder.clearContext();
            entryPoint.commence(request, response, e);
            return;
        } catch (Exception e) {
            logger.warn("Failed to authenticate: " + e.getMessage());

            SecurityContextHolder.clearContext();
            entryPoint.commence(request, response, new AuthenticationServiceException(e.getMessage()));
            return;
        }

        logger.debug("Authentication successful!");

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        logger.debug("Checking URI access...");

        boolean matched = skipMatcher != null && skipMatcher.matches(request);

        logger.debug(matched ? "Authentication filter skipped" : "Authentication filter continue...");

        return matched;
    }
}
