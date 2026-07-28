package me.melkx.authmodule.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DelegatingAuthenticationFilterTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AuthenticationConverter authenticationConverter;
    @Mock
    private AuthenticationEntryPoint entryPoint;
    @Mock
    private RequestMatcher matcher;

    private DelegatingAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        filter = new DelegatingAuthenticationFilter(
                authenticationManager,
                authenticationConverter,
                entryPoint,
                matcher
        );
    }

    @Test
    void constructor_ShouldThrowException_WhenAuthenticationManagerIsNull() {
        assertThatThrownBy(() -> new DelegatingAuthenticationFilter(
                null, authenticationConverter, entryPoint, matcher))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("authenticationManager cannot be null");
    }

    @Test
    void constructor_ShouldThrowException_WhenAuthenticationConverterIsNull() {
        assertThatThrownBy(() -> new DelegatingAuthenticationFilter(
                authenticationManager, null, entryPoint, matcher))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("authenticationConverter cannot be null");
    }

    @Test
    void constructor_ShouldThrowException_WhenEntryPointIsNull() {
        assertThatThrownBy(() -> new DelegatingAuthenticationFilter(
                authenticationManager, authenticationConverter, null, matcher))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("entryPoint cannot be null");
    }

    @Test
    void constructor_ShouldNotThrowException_WhenSkipMatcherIsNull() {
        assertThatCode(() -> new DelegatingAuthenticationFilter(
                authenticationManager, authenticationConverter, entryPoint, null))
                .doesNotThrowAnyException();
    }

    @Test
    void doFilterInternal_ShouldCommence_WhenAuthenticationConverterThrowsException() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(authenticationConverter.convert(request))
                .thenThrow(BadCredentialsException.class);

        filter.doFilterInternal(request, response, filterChain);

        verify(entryPoint).commence(eq(request), eq(response), any());
    }

    @Test
    void doFilterInternal_ShouldDoFilter_WhenAuthRequestIsNull() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(authenticationConverter.convert(request))
                .thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_ShouldCommence_WhenAuthenticationManagerThrowsException() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        TestingAuthenticationToken authenticationToken =
                new TestingAuthenticationToken(null, null);

        when(authenticationConverter.convert(request))
                .thenReturn(authenticationToken);

        when(authenticationManager.authenticate(authenticationToken))
                .thenThrow(AuthenticationServiceException.class);

        filter.doFilterInternal(request, response, filterChain);

        verify(entryPoint).commence(eq(request), eq(response), any());
    }

    @Test
    void doFilterInternal_ShouldAuthenticateSuccessfully() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        TestingAuthenticationToken authRequest = new TestingAuthenticationToken("user", "pass");
        TestingAuthenticationToken authResult = new TestingAuthenticationToken("user", "pass", List.of(() -> "ROLE_USER"));

        when(authenticationConverter.convert(request)).thenReturn(authRequest);
        when(authenticationManager.authenticate(authRequest)).thenReturn(authResult);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isEqualTo(authResult);
    }

    @Test
    void doFilterInternal_ShouldCommence_WhenUnexpectedExceptionThrown() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(authenticationConverter.convert(request)).thenThrow(RuntimeException.class);

        filter.doFilterInternal(request, response, filterChain);

        verify(entryPoint).commence(eq(request), eq(response), any(AuthenticationServiceException.class));
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void shouldNotFilter_ShouldReturnFalse_WhenSkipMatcherIsNull() {
        DelegatingAuthenticationFilter filterWithoutMatcher = new DelegatingAuthenticationFilter(
                authenticationManager, authenticationConverter, entryPoint, null);

        HttpServletRequest request = mock(HttpServletRequest.class);

        boolean matched = filterWithoutMatcher.shouldNotFilter(request);

        assertThat(matched).isFalse();
        verifyNoInteractions(request);
    }

    @Test
    void shouldNotFilter_ShouldReturnTrue_WhenUriMatch() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(matcher.matches(request))
                .thenReturn(true);

        boolean matched = filter.shouldNotFilter(request);

        assertThat(matched)
                .isEqualTo(true);
    }
}