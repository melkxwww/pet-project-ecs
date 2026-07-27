package me.melkx.jwtmodule.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.Keys;
import me.melkx.jwtmodule.dto.JsonTokenType;
import me.melkx.jwtmodule.dto.TokenPayload;
import me.melkx.jwtmodule.properties.JwtValidityTimeProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

class JwtGeneratorTest {
    private static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor("12345678901234567890123456789012".getBytes(StandardCharsets.UTF_8));

    private static final JwtValidityTimeProperties VALIDITY_TIME_PROPERTIES =
            new JwtValidityTimeProperties(100, 10_000);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final JwtGenerator GENERATOR = new JwtGenerator(
            SECRET_KEY, VALIDITY_TIME_PROPERTIES, OBJECT_MAPPER
    );

    @ParameterizedTest
    @MethodSource("provideGeneratorsParams")
    void createJwtGeneratorConstructor_ShouldThrowException_WhenAnyParamIsNull(JwtGeneratorParams params) {
        assertThatThrownBy(() -> new JwtGenerator(params.secretKey(), params.validityTimeProperties(), params.objectMapper()))
                .isInstanceOf(NullPointerException.class);
    }

    static List<JwtGeneratorParams> provideGeneratorsParams() {
        return List.of(
                new JwtGeneratorParams(null, VALIDITY_TIME_PROPERTIES, OBJECT_MAPPER),
                new JwtGeneratorParams(SECRET_KEY, null, OBJECT_MAPPER),
                new JwtGeneratorParams(SECRET_KEY, VALIDITY_TIME_PROPERTIES, null)
        );
    }

    @Test
    void generateAccessToken_ShouldThrowException_WhenPayloadIsNull() {
        assertThatThrownBy(() -> GENERATOR.generateAccessToken(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("payload cannot be null");
    }

    @Test
    void generateRefreshToken_ShouldThrowException_WhenPayloadIsNull() {
        assertThatThrownBy(() -> GENERATOR.generateRefreshToken(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("payload cannot be null");
    }

    @Test
    void generateAccessToken_ShouldThrowException_WhenTokenTypeInPayloadIsNotCorrespondsWithProvidedTokenType() {
        RefreshTokenPayload payload = new RefreshTokenPayload(UUID.randomUUID());

        assertThatThrownBy(() -> GENERATOR.generateAccessToken(payload))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("tokenType in payload is not corresponds with provided tokenType");
    }

    @Test
    void generateRefreshToken_ShouldThrowException_WhenTokenTypeInPayloadIsNotCorrespondsWithProvidedTokenType() {
        AccessTokenPayload payload = new AccessTokenPayload(UUID.randomUUID());

        assertThatThrownBy(() -> GENERATOR.generateRefreshToken(payload))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("tokenType in payload is not corresponds with provided tokenType");
    }

    @Test
    void generateAccessToken_ShouldReturnValidToken() {
        AccessTokenPayload payload = new AccessTokenPayload(UUID.randomUUID());

        String token = GENERATOR.generateAccessToken(payload);

        assertThat(token).isNotNull();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    void generateRefreshToken_ShouldReturnValidToken() {
        RefreshTokenPayload payload = new RefreshTokenPayload(UUID.randomUUID());

        String token = GENERATOR.generateRefreshToken(payload);

        assertThat(token).isNotNull();
        assertThat(token.split("\\.")).hasSize(3);
    }

    record JwtGeneratorParams(SecretKey secretKey, JwtValidityTimeProperties validityTimeProperties,
                              ObjectMapper objectMapper) {
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