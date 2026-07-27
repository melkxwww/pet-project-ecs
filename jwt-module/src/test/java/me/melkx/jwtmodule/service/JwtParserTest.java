package me.melkx.jwtmodule.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.security.Keys;
import me.melkx.jwtmodule.dto.JsonTokenType;
import me.melkx.jwtmodule.dto.TokenPayload;
import me.melkx.jwtmodule.properties.JwtValidityTimeProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

class JwtParserTest {
    private static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor("12345678901234567890123456789012".getBytes(StandardCharsets.UTF_8));

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final JwtParser PARSER = new JwtParser(SECRET_KEY, OBJECT_MAPPER);

    @ParameterizedTest
    @MethodSource("provideJwtParsersParams")
    void createJwtParserConstructor_ShouldThrowException_WhenAnyParamIsNull(JwtParserParams params) {
        assertThatThrownBy(() -> new JwtParser(params.secretKey(), params.objectMapper()))
                .isInstanceOf(NullPointerException.class);
    }

    static List<JwtParserParams> provideJwtParsersParams() {
        return List.of(
                new JwtParserParams(null, OBJECT_MAPPER),
                new JwtParserParams(SECRET_KEY, null)
        );
    }

    @Test
    void parse_ShouldThrowException_WhenTokenOrTargetIsNull() {
        assertThatThrownBy(() -> PARSER.parse(null, TestTokenPayload.class))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("token cannot be null");
        assertThatThrownBy(() -> PARSER.parse("", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("target cannot be null");
    }

    @Test
    void parse_ShouldReturnFailureParseResult_WhenTokenHasExpired() {
        Instant fixedInstant = Instant.now().minus(10, ChronoUnit.MINUTES);

        try (MockedStatic<Instant> instantMock = mockStatic(Instant.class)) {
            instantMock.when(Instant::now).thenReturn(fixedInstant);

            JwtGenerator generator = createGenerator(false);

            String token = generator.generateAccessToken(
                    new TestTokenPayload()
            );

            ParseResult<TestTokenPayload> result = PARSER.parse(token, TestTokenPayload.class);

            assertThat(result)
                    .isNotNull()
                    .extracting(ParseResult::valid, ParseResult::errorMessage)
                    .containsExactly(false, "Token has expired");
        }
    }

    @Test
    void parse_ShouldReturnFailureParseResult_WhenTokenHasInvalidFormat() {
        String invalidToken = "a.b.c";

        ParseResult<TestTokenPayload> result = PARSER.parse(invalidToken, TestTokenPayload.class);

        assertThat(result)
                .isNotNull()
                .extracting(ParseResult::valid, ParseResult::errorMessage)
                .containsExactly(false, "Invalid token format");
    }

    @Test
    void parse_ShouldReturnFailureParseResult_WhenTokenHasInvalidSignature() {
        JwtGenerator generator = createGenerator(true);

        String tokenWithInvalidSig = generator.generateAccessToken(new TestTokenPayload());

        ParseResult<TestTokenPayload> result = PARSER.parse(tokenWithInvalidSig, TestTokenPayload.class);

        assertThat(result)
                .isNotNull()
                .extracting(ParseResult::valid, ParseResult::errorMessage)
                .containsExactly(false, "Invalid signature");
    }

    @Test
    void parse_ShouldReturnSuccessParseResult() {
        JwtGenerator generator = createGenerator(false);

        TestTokenPayload creationPayload = new TestTokenPayload();
        String token = generator.generateAccessToken(creationPayload);

        ParseResult<TestTokenPayload> result = PARSER.parse(token, TestTokenPayload.class);
        TestTokenPayload parsedPayload = result.result();

        assertThat(result)
                .isNotNull()
                .extracting(ParseResult::valid)
                .isEqualTo(true);

        assertThat(parsedPayload)
                .isNotNull()
                .extracting(TestTokenPayload::sub)
                .isEqualTo(creationPayload.sub());
    }

    JwtGenerator createGenerator(boolean invalid) {
        return new JwtGenerator(
                invalid
                        ? Keys.hmacShaKeyFor("abcdefghijklmnopqrstuvwxyz123456".getBytes(StandardCharsets.UTF_8))
                        : SECRET_KEY,
                new JwtValidityTimeProperties(60, 60),
                OBJECT_MAPPER
        );
    }

    record JwtParserParams(SecretKey secretKey, ObjectMapper objectMapper) {
    }

    record TestTokenPayload(UUID sub, Long iat, Long exp) implements TokenPayload {
        public TestTokenPayload() {
            this(UUID.randomUUID(), null, null);
        }

        @Override
        public JsonTokenType getTokenType() {
            return JsonTokenType.ACCESS;
        }
    }
}