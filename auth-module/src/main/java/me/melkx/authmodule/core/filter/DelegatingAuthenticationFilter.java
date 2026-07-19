package me.melkx.authmodule.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.melkx.authmodule.core.exception.InternalAuthenticationException;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class DelegatingAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final AuthenticationConverter authenticationConverter;
    private final AuthenticationEntryPoint entryPoint;
    private final @Nullable RequestMatcher skipMatcher;

    public DelegatingAuthenticationFilter(AuthenticationManager authenticationManager,
                                          AuthenticationConverter authenticationConverter,
                                          AuthenticationEntryPoint entryPoint,
                                          @Nullable RequestMatcher skipMatcher) {
        this.authenticationManager = authenticationManager;
        this.authenticationConverter = authenticationConverter;
        this.entryPoint = entryPoint;
        this.skipMatcher = skipMatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication authRequest = authenticationConverter.convert(request);

            if (authRequest == null) {
                filterChain.doFilter(request, response);
                return;
            }

            Authentication authResult = authenticationManager.authenticate(authRequest);
            SecurityContextHolder.getContext().setAuthentication(authResult);
        } catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            entryPoint.commence(request, response, e);
            return;
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            entryPoint.commence(request, response, new InternalAuthenticationException(e.getMessage()));
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return skipMatcher != null && skipMatcher.matches(request);
    }
}
