package me.melkx.authmodule.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.melkx.shared.FormattedError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import tools.jackson.databind.ObjectMapper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormattedAuthenticationEntryPointTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private ObjectMapper mockObjectMapper;

    private ObjectMapper realObjectMapper;
    private FormattedAuthenticationEntryPoint entryPoint;

    @BeforeEach
    void setUp() {
        realObjectMapper = new ObjectMapper();
        entryPoint = new FormattedAuthenticationEntryPoint(realObjectMapper);
    }

    // TESTS FOR CONSTRUCTOR

    @Test
    void constructor_ShouldThrowException_WhenObjectMapperIsNull() {
        assertThatThrownBy(() -> new FormattedAuthenticationEntryPoint(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("objectMapper cannot be null");
    }

    @Test
    void constructor_ShouldNotThrowException_WhenObjectMapperIsValid() {
        assertThatCode(() -> new FormattedAuthenticationEntryPoint(realObjectMapper))
                .doesNotThrowAnyException();
    }

    // TESTS FOR COMMENCE METHOD

    @Test
    void commence_ShouldReturnUnauthorizedStatus_WhenAuthenticationExceptionOccurs() throws Exception {
        String errorMessage = "Invalid credentials";
        String requestURI = "/api/test";
        AuthenticationException authException = new AuthenticationException(errorMessage) {};

        when(request.getRequestURI()).thenReturn(requestURI);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        entryPoint.commence(request, response, authException);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(response).getWriter();

        String jsonResponse = stringWriter.toString();
        assertThat(jsonResponse).isNotEmpty();

        FormattedError error = realObjectMapper.readValue(jsonResponse, FormattedError.class);
        assertThat(error)
                .extracting(
                        FormattedError::error,
                        FormattedError::status,
                        FormattedError::message,
                        FormattedError::path,
                        FormattedError::timestamp
                )
                .containsExactly(
                        "Unauthorized",
                        401,
                        errorMessage,
                        requestURI,
                        error.timestamp()
                );
        assertThat(error.timestamp()).isNotNull();
    }

    @Test
    void commence_ShouldReturnInternalServerErrorStatus_WhenAuthenticationServiceExceptionOccurs() throws Exception {
        String errorMessage = "Authentication service unavailable";
        String requestURI = "/api/test";
        AuthenticationServiceException authException = new AuthenticationServiceException(errorMessage);

        when(request.getRequestURI()).thenReturn(requestURI);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        entryPoint.commence(request, response, authException);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(response).setContentType("application/json");
        verify(response).getWriter();

        String jsonResponse = stringWriter.toString();
        assertThat(jsonResponse).isNotEmpty();

        FormattedError error = realObjectMapper.readValue(jsonResponse, FormattedError.class);
        assertThat(error)
                .extracting(
                        FormattedError::error,
                        FormattedError::status,
                        FormattedError::message,
                        FormattedError::path
                )
                .containsExactly(
                        "Internal Server Error",
                        500,
                        errorMessage,
                        requestURI
                );
    }

    @Test
    void commence_ShouldHandleExceptionWithNullMessage() throws Exception {
        String requestURI = "/api/test";
        AuthenticationException authException = new AuthenticationException((String) null) {};

        when(request.getRequestURI()).thenReturn(requestURI);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        entryPoint.commence(request, response, authException);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");

        String jsonResponse = stringWriter.toString();
        FormattedError error = realObjectMapper.readValue(jsonResponse, FormattedError.class);
        assertThat(error.message()).isNull();
    }

    @Test
    void commence_ShouldSetCorrectContentTypeAndStatus() throws Exception {
        AuthenticationException authException = new AuthenticationException("Test") {};
        String requestURI = "/api/test";

        when(request.getRequestURI()).thenReturn(requestURI);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        entryPoint.commence(request, response, authException);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        verify(response).getWriter();
    }

    @Test
    void commence_ShouldWriteValidJsonResponse() throws Exception {
        String errorMessage = "Authentication failed";
        String requestURI = "/api/protected/resource";
        AuthenticationException authException = new AuthenticationException(errorMessage) {};

        when(request.getRequestURI()).thenReturn(requestURI);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        entryPoint.commence(request, response, authException);

        String jsonResponse = stringWriter.toString();

        assertThat(jsonResponse)
                .contains("\"error\"")
                .contains("\"status\"")
                .contains("\"message\"")
                .contains("\"path\"")
                .contains("\"timestamp\"");

        assertThat(jsonResponse)
                .contains("\"error\":\"Unauthorized\"")
                .contains("\"status\":401")
                .contains("\"message\":\"" + errorMessage + "\"")
                .contains("\"path\":\"" + requestURI + "\"");
    }

    @Test
    void commence_ShouldHandleIOException_WhenWriterThrowsException() throws Exception {
        AuthenticationException authException = new AuthenticationException("Test") {};
        String requestURI = "/api/test";

        when(request.getRequestURI()).thenReturn(requestURI);
        when(response.getWriter()).thenThrow(new RuntimeException("IO Error"));

        assertThatThrownBy(() -> entryPoint.commence(request, response, authException))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("IO Error");
    }

    @Test
    void commence_ShouldIncludeTimestampInResponse() throws Exception {
        AuthenticationException authException = new AuthenticationException("Test") {};
        String requestURI = "/api/test";

        when(request.getRequestURI()).thenReturn(requestURI);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        entryPoint.commence(request, response, authException);

        String jsonResponse = stringWriter.toString();
        FormattedError error = realObjectMapper.readValue(jsonResponse, FormattedError.class);

        assertThat(error.timestamp()).isNotNull();
        assertThat(error.timestamp()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    // EDGE CASE TESTS

    @Test
    void commence_ShouldHandleEmptyRequestURI() throws Exception {
        AuthenticationException authException = new AuthenticationException("Test") {};
        when(request.getRequestURI()).thenReturn("");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        entryPoint.commence(request, response, authException);

        String jsonResponse = stringWriter.toString();
        FormattedError error = realObjectMapper.readValue(jsonResponse, FormattedError.class);
        assertThat(error.path()).isEmpty();
    }

    @Test
    void commence_ShouldHandleNullRequestURI() throws Exception {
        AuthenticationException authException = new AuthenticationException("Test") {};
        when(request.getRequestURI()).thenReturn(null);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        entryPoint.commence(request, response, authException);

        String jsonResponse = stringWriter.toString();
        FormattedError error = realObjectMapper.readValue(jsonResponse, FormattedError.class);
        assertThat(error.path()).isNull();
    }

    @Test
    void commence_ShouldHandleAuthenticationExceptionWithCustomMessage() throws Exception {
        String customMessage = "Custom authentication error: token expired";
        AuthenticationException authException = new AuthenticationException(customMessage) {};
        String requestURI = "/api/secure";

        when(request.getRequestURI()).thenReturn(requestURI);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        entryPoint.commence(request, response, authException);

        String jsonResponse = stringWriter.toString();
        FormattedError error = realObjectMapper.readValue(jsonResponse, FormattedError.class);
        assertThat(error.message()).isEqualTo(customMessage);
    }
}