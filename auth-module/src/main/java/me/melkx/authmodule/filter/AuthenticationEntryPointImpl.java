package me.melkx.authmodule.filter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.melkx.authmodule.exception.UnauthorizedAuthenticationException;
import me.melkx.share.FormattedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthenticationEntryPointImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String message = authException.getMessage();
        String path = request.getRequestURI();
        LocalDateTime timestamp = LocalDateTime.now();
        HttpStatus status;

        if (authException instanceof UnauthorizedAuthenticationException) {
            status = HttpStatus.UNAUTHORIZED;
        }
        else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        response.setStatus(status.value());
        response.getWriter().write(objectMapper.writeValueAsString(
                new FormattedError(
                        status.getReasonPhrase(),
                        status.value(),
                        message,
                        path,
                        timestamp
                )
        ));
    }
}
