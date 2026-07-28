package me.melkx.authmodule.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.melkx.shared.FormattedError;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
public class FormattedAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public FormattedAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper cannot be null");
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.debug("Entry point executing...");

        HttpStatus status = resolveHttpStatus(authException);

        log.debug("Resolved status: {}", status);

        FormattedError error = new FormattedError(
                status.getReasonPhrase(),
                status.value(),
                authException.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }

    private HttpStatus resolveHttpStatus(AuthenticationException exception) {
        if (exception instanceof AuthenticationServiceException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.UNAUTHORIZED;
    }
}