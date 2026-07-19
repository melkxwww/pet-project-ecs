package me.melkx.authmodule.core.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.melkx.authmodule.core.exception.InternalAuthenticationException;
import me.melkx.shared.FormattedError;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        HttpStatus status = resolveHttpStatus(authException);

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
        if (exception instanceof InternalAuthenticationException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.UNAUTHORIZED;
    }
}