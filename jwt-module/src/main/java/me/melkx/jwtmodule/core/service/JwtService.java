package me.melkx.jwtmodule.core.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import me.melkx.jwtmodule.core.dto.TokenPayload;
import me.melkx.jwtmodule.core.exception.JwtProcessingException;
import me.melkx.jwtmodule.core.exception.JwtInvalidTokenException;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class JwtService {
    private static final ObjectMapper OBJECT_MAPPER;

    private final SecretKey secretKey;

    static {
        OBJECT_MAPPER = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
    }

    public JwtService(SecretKey secretKey) {
        Objects.requireNonNull(secretKey, "secretKey cannot be null");

        this.secretKey = secretKey;
    }

    public String generate(TokenPayload payload, Duration validity) throws JwtProcessingException {
        Objects.requireNonNull(payload, "payload cannot be null");
        Objects.requireNonNull(validity, "validity cannot be null");

        if (validity.isNegative() || validity.isZero()) {
            throw new IllegalArgumentException("validity must be positive");
        }

        try {
            Map<String, Object> claims = OBJECT_MAPPER.convertValue(payload, Map.class);

            Instant now = Instant.now();

            return Jwts.builder()
                    .claims(claims)
                    .issuedAt(Date.from(now))
                    .expiration(Date.from(now.plus(validity)))
                    .signWith(secretKey)
                    .compact();

        } catch (IllegalArgumentException e) {
            throw new JwtProcessingException("Failed to convert payload to claims", e);
        } catch (JwtException e) {
            throw new JwtProcessingException("Failed to generate JWT token", e);
        }
    }

    public <T extends TokenPayload> T read(String token, Class<T> target) throws JwtInvalidTokenException {
        Objects.requireNonNull(token, "token cannot be null");

        try {
            String unsecuredToken = stripSignature(token);

            Claims claims = Jwts.parser()
                    .build()
                    .parseUnsecuredClaims(unsecuredToken)
                    .getPayload();

            return OBJECT_MAPPER.convertValue(claims, target);
        } catch (JwtException e) {
            throw new JwtInvalidTokenException("Invalid token structure", e);
        } catch (IllegalArgumentException e) {
            throw new JwtInvalidTokenException("Failed to map token claims to " + target.getSimpleName(), e);
        }
    }

    public ValidationResult validate(String token) {
        Objects.requireNonNull(token, "token cannot be null");

        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return ValidationResult.success();
        } catch (ExpiredJwtException e) {
            return ValidationResult.failure("Token has expired");
        } catch (MalformedJwtException e) {
            return ValidationResult.failure("Invalid token format");
        } catch (SignatureException e) {
            return ValidationResult.failure("Invalid signature");
        } catch (JwtException e) {
            return ValidationResult.failure("Token validation failed");
        }
    }

    private String stripSignature(String token) {
        int lastDot = token.lastIndexOf('.');
        if (lastDot <= 0) {
            return token;
        }

        return token.substring(0, lastDot);
    }
}
