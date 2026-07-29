package me.melkx.jwtmodule.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.Keys;
import me.melkx.jwtmodule.dto.JsonTokenType;
import me.melkx.jwtmodule.dto.TokenPayload;
import me.melkx.jwtmodule.exception.JwtProcessingException;
import me.melkx.jwtmodule.properties.JwtValidityTimeProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtGeneratorTest {
    private static final String SECRET_KEY_STRING = "12345678901234567890123456789012";

    private SecretKey secretKey;
    private JwtValidityTimeProperties validityTimeProperties;
    private ObjectMapper objectMapper;
    private JwtGenerator generator;

    @Mock
    private JwtValidityTimeProperties mockValidityTimeProperties;

    @Mock
    private ObjectMapper mockObjectMapper;

    @BeforeEach
    void setUp() {
        secretKey = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8));
        validityTimeProperties = new JwtValidityTimeProperties(100, 10_000);
        objectMapper = new ObjectMapper();
        generator = new JwtGenerator(secretKey, validityTimeProperties, objectMapper);
    }

    // TESTS FOR CONSTRUCTOR

    @Test
    void constructor_ShouldThrowException_WhenSecretKeyIsNull() {
        assertThatThrownBy(() -> new JwtGenerator(
                null, mockValidityTimeProperties, mockObjectMapper))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("secretKey cannot be null");
    }

    @Test
    void constructor_ShouldThrowException_WhenValidityTimePropertiesIsNull() {
        assertThatThrownBy(() -> new JwtGenerator(
                mock(SecretKey.class), null, mockObjectMapper))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("validityTimeProperties cannot be null");
    }

    @Test
    void constructor_ShouldThrowException_WhenObjectMapperIsNull() {
        assertThatThrownBy(() -> new JwtGenerator(
                mock(SecretKey.class), mockValidityTimeProperties, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("objectMapper cannot be null");
    }

    // TESTS FOR GENERATE ACCESS TOKEN

    @Test
    void generateAccessToken_ShouldThrowException_WhenPayloadIsNull() {
        assertThatThrownBy(() -> generator.generateAccessToken(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("payload cannot be null");
    }

    @Test
    void generateAccessToken_ShouldThrowException_WhenTokenTypeInPayloadIsNotCorrespondsWithProvidedTokenType() {
        RefreshTokenPayload payload = new RefreshTokenPayload(UUID.randomUUID());

        assertThatThrownBy(() -> generator.generateAccessToken(payload))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("tokenType in payload is not corresponds with provided tokenType");
    }

    @Test
    void generateAccessToken_ShouldReturnValidToken() {
        AccessTokenPayload payload = new AccessTokenPayload(UUID.randomUUID());

        String token = generator.generateAccessToken(payload);

        assertThat(token).isNotNull();
        assertThat(token.split("\\.")).hasSize(3);
    }

    // TESTS FOR GENERATE REFRESH TOKEN

    @Test
    void generateRefreshToken_ShouldThrowException_WhenPayloadIsNull() {
        assertThatThrownBy(() -> generator.generateRefreshToken(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("payload cannot be null");
    }

    @Test
    void generateRefreshToken_ShouldThrowException_WhenTokenTypeInPayloadIsNotCorrespondsWithProvidedTokenType() {
        AccessTokenPayload payload = new AccessTokenPayload(UUID.randomUUID());

        assertThatThrownBy(() -> generator.generateRefreshToken(payload))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("tokenType in payload is not corresponds with provided tokenType");
    }

    @Test
    void generateRefreshToken_ShouldReturnValidToken() {
        RefreshTokenPayload payload = new RefreshTokenPayload(UUID.randomUUID());

        String token = generator.generateRefreshToken(payload);

        assertThat(token).isNotNull();
        assertThat(token.split("\\.")).hasSize(3);
    }

    // TESTS FOR VALIDITY PERIOD

    @Test
    void generateAccessToken_ShouldThrowException_WhenValidityIsZero() {
        JwtValidityTimeProperties zeroValidity = new JwtValidityTimeProperties(0, 0);
        JwtGenerator generatorWithZeroValidity = new JwtGenerator(
                secretKey, zeroValidity, objectMapper);
        AccessTokenPayload payload = new AccessTokenPayload(UUID.randomUUID());

        assertThatThrownBy(() -> generatorWithZeroValidity.generateAccessToken(payload))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("validity must be positive");
    }

    @Test
    void generateRefreshToken_ShouldThrowException_WhenValidityIsZero() {
        JwtValidityTimeProperties zeroValidity = new JwtValidityTimeProperties(0, 0);
        JwtGenerator generatorWithZeroValidity = new JwtGenerator(
                secretKey, zeroValidity, objectMapper);
        RefreshTokenPayload payload = new RefreshTokenPayload(UUID.randomUUID());

        assertThatThrownBy(() -> generatorWithZeroValidity.generateRefreshToken(payload))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("validity must be positive");
    }

    @Test
    void generateAccessToken_ShouldThrowException_WhenValidityIsNegative() {
        JwtValidityTimeProperties negativeValidity = new JwtValidityTimeProperties(-100, -1000);
        JwtGenerator generatorWithNegativeValidity = new JwtGenerator(
                secretKey, negativeValidity, objectMapper);
        AccessTokenPayload payload = new AccessTokenPayload(UUID.randomUUID());

        assertThatThrownBy(() -> generatorWithNegativeValidity.generateAccessToken(payload))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("validity must be positive");
    }

    // TESTS FOR EXCEPTIONS

    @Test
    void generateAccessToken_ShouldThrowJwtProcessingException_WhenPayloadConversionFails() {
        TokenPayload invalidPayload = mock(TokenPayload.class);
        when(invalidPayload.getTokenType()).thenReturn(JsonTokenType.ACCESS);

        ObjectMapper failingObjectMapper = mock(ObjectMapper.class);
        when(failingObjectMapper.convertValue(invalidPayload, Map.class))
                .thenThrow(new IllegalArgumentException("Conversion failed"));

        JwtGenerator generatorWithFailingMapper = new JwtGenerator(
                secretKey, validityTimeProperties, failingObjectMapper);

        assertThatThrownBy(() -> generatorWithFailingMapper.generateAccessToken(invalidPayload))
                .isInstanceOf(JwtProcessingException.class)
                .hasMessageContaining("Failed to convert payload to claims")
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void generateRefreshToken_ShouldThrowJwtProcessingException_WhenPayloadConversionFails() {
        TokenPayload invalidPayload = mock(TokenPayload.class);
        when(invalidPayload.getTokenType()).thenReturn(JsonTokenType.REFRESH);

        ObjectMapper failingObjectMapper = mock(ObjectMapper.class);
        when(failingObjectMapper.convertValue(invalidPayload, Map.class))
                .thenThrow(new IllegalArgumentException("Conversion failed"));

        JwtGenerator generatorWithFailingMapper = new JwtGenerator(
                secretKey, validityTimeProperties, failingObjectMapper);

        assertThatThrownBy(() -> generatorWithFailingMapper.generateRefreshToken(invalidPayload))
                .isInstanceOf(JwtProcessingException.class)
                .hasMessageContaining("Failed to convert payload to claims")
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    record AccessTokenPayload(UUID sub) implements TokenPayload {
        @Override
        public JsonTokenType getTokenType() {
            return JsonTokenType.ACCESS;
        }
    }

    record RefreshTokenPayload(UUID sub) implements TokenPayload {
        @Override
        public JsonTokenType getTokenType() {
            return JsonTokenType.REFRESH;
        }
    }
}