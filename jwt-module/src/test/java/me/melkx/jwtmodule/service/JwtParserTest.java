package me.melkx.jwtmodule.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import me.melkx.jwtmodule.dto.JsonTokenType;
import me.melkx.jwtmodule.dto.TokenPayload;
import me.melkx.jwtmodule.properties.JwtValidityTimeProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtParserTest {
    private static final String SECRET_KEY_STRING = "12345678901234567890123456789012";

    private SecretKey secretKey;
    private ObjectMapper objectMapper;
    private JwtParser parser;
    private JwtGenerator generator;

    @Mock
    private ObjectMapper mockObjectMapper;

    @BeforeEach
    void setUp() {
        secretKey = Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes(StandardCharsets.UTF_8));
        objectMapper = new ObjectMapper();
        parser = new JwtParser(secretKey, objectMapper);

        JwtValidityTimeProperties validityTimeProperties = new JwtValidityTimeProperties(60, 60);
        generator = new JwtGenerator(secretKey, validityTimeProperties, objectMapper);
    }

    // TESTS FOR CONSTRUCTOR

    @Test
    void constructor_ShouldThrowException_WhenSecretKeyIsNull() {
        assertThatThrownBy(() -> new JwtParser(null, mockObjectMapper))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("secretKey cannot be null");
    }

    @Test
    void constructor_ShouldThrowException_WhenObjectMapperIsNull() {
        assertThatThrownBy(() -> new JwtParser(mock(SecretKey.class), null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("objectMapper cannot be null");
    }

    // TESTS FOR PARSE METHOD

    @Test
    void parse_ShouldThrowException_WhenTokenIsNull() {
        assertThatThrownBy(() -> parser.parse(null, TestTokenPayload.class))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("token cannot be null");
    }

    @Test
    void parse_ShouldThrowException_WhenTargetIsNull() {
        assertThatThrownBy(() -> parser.parse("valid.token", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("target cannot be null");
    }

    @Test
    void parse_ShouldReturnFailureParseResult_WhenTokenHasExpired() {
        String expiredToken = Jwts.builder()
                .subject("test")
                .issuedAt(Date.from(Instant.now().minus(20, ChronoUnit.MINUTES)))
                .expiration(Date.from(Instant.now().minus(10, ChronoUnit.MINUTES)))
                .signWith(secretKey)
                .compact();

        ParseResult<TestTokenPayload> result = parser.parse(expiredToken, TestTokenPayload.class);

        assertThat(result)
                .isNotNull()
                .extracting(ParseResult::valid, ParseResult::errorMessage)
                .containsExactly(false, "Token has expired");
    }

    @ParameterizedTest
    @CsvSource({
            "invalid.token.format",
            "a.b.c.d.e",
            "1234567890"
    })
    void parse_ShouldReturnFailureParseResult_WhenTokenHasInvalidFormat(String invalidToken) {
        ParseResult<TestTokenPayload> result = parser.parse(invalidToken, TestTokenPayload.class);

        assertThat(result)
                .isNotNull()
                .extracting(ParseResult::valid, ParseResult::errorMessage)
                .containsExactly(false, "Invalid token format");
    }

    @Test
    void parse_ShouldReturnFailureParseResult_WhenTokenValidationFails() {
        String invalidToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.invalid_signature";

        ParseResult<TestTokenPayload> result = parser.parse(invalidToken, TestTokenPayload.class);

        assertThat(result)
                .isNotNull()
                .extracting(ParseResult::valid, ParseResult::errorMessage)
                .containsExactly(false, "Invalid signature");
    }

    @Test
    void parse_ShouldReturnFailureParseResult_WhenTokenHasInvalidSignature() {
        SecretKey differentKey = Keys.hmacShaKeyFor(
                "abcdefghijklmnopqrstuvwxyz123456".getBytes(StandardCharsets.UTF_8)
        );
        JwtValidityTimeProperties validityTimeProperties = new JwtValidityTimeProperties(60, 60);
        JwtGenerator differentGenerator = new JwtGenerator(
                differentKey, validityTimeProperties, objectMapper
        );

        String tokenWithDifferentSignature = differentGenerator.generateAccessToken(
                new TestTokenPayload()
        );

        ParseResult<TestTokenPayload> result = parser.parse(
                tokenWithDifferentSignature,
                TestTokenPayload.class
        );

        assertThat(result)
                .isNotNull()
                .extracting(ParseResult::valid, ParseResult::errorMessage)
                .containsExactly(false, "Invalid signature");
    }

    @Test
    void parse_ShouldReturnFailureParseResult_WhenTokenHasEmptyClaims() {
        String emptyClaimsToken = Jwts.builder()
                .signWith(secretKey)
                .compact();

        ParseResult<TestTokenPayload> result = parser.parse(emptyClaimsToken, TestTokenPayload.class);

        assertThat(result)
                .isNotNull()
                .extracting(ParseResult::valid)
                .isEqualTo(false);
        assertThat(result.errorMessage())
                .contains("Token validation failed");
    }

    // TESTS FOR EXCEPTION HANDLING

    @Test
    void parse_ShouldReturnFailureParseResult_WhenObjectMapperConversionFails() {
        ObjectMapper failingObjectMapper = mock(ObjectMapper.class);
        when(failingObjectMapper.convertValue(any(), eq(TestTokenPayload.class)))
                .thenThrow(new IllegalArgumentException("Conversion failed"));

        JwtParser parserWithFailingMapper = new JwtParser(secretKey, failingObjectMapper);

        TestTokenPayload payload = new TestTokenPayload();
        String token = generator.generateAccessToken(payload);

        ParseResult<TestTokenPayload> result = parserWithFailingMapper.parse(
                token,
                TestTokenPayload.class
        );

        assertThat(result)
                .isNotNull()
                .extracting(ParseResult::valid, ParseResult::errorMessage)
                .containsExactly(false, "Token validation failed");
    }

    @Test
    void parse_ShouldReturnFailureParseResult_WhenClaimsAreNotConvertible() {
        ObjectMapper failingObjectMapper = mock(ObjectMapper.class);
        when(failingObjectMapper.convertValue(any(Claims.class), eq(TestTokenPayload.class)))
                .thenThrow(new IllegalArgumentException("Cannot convert claims"));

        JwtParser parserWithFailingMapper = new JwtParser(secretKey, failingObjectMapper);

        TestTokenPayload payload = new TestTokenPayload();
        String token = generator.generateAccessToken(payload);

        ParseResult<TestTokenPayload> result = parserWithFailingMapper.parse(
                token,
                TestTokenPayload.class
        );

        assertThat(result)
                .isNotNull()
                .extracting(ParseResult::valid, ParseResult::errorMessage)
                .containsExactly(false, "Token validation failed");
    }

    @Test
    void parse_ShouldReturnSuccessParseResult_WhenTokenIsValid() {
        TestTokenPayload creationPayload = new TestTokenPayload();
        String token = generator.generateAccessToken(creationPayload);

        ParseResult<TestTokenPayload> result = parser.parse(token, TestTokenPayload.class);
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