package me.melkx.jwtmodule.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import me.melkx.jwtmodule.dto.JsonTokenType;
import me.melkx.jwtmodule.dto.TokenPayload;
import me.melkx.jwtmodule.exception.JwtProcessingException;
import me.melkx.jwtmodule.properties.JwtValidityTimeProperties;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class JwtGenerator {
    private final SecretKey secretKey;
    private final JwtValidityTimeProperties validityTimeProperties;
    private final ObjectMapper objectMapper;

    public JwtGenerator(SecretKey secretKey, JwtValidityTimeProperties validityTimeProperties, ObjectMapper objectMapper) {
        this.secretKey = Objects.requireNonNull(secretKey, "secretKey cannot be null");
        this.validityTimeProperties = Objects.requireNonNull(validityTimeProperties, "validityTimeProperties cannot be null");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper cannot be null");
    }

    public String generateAccessToken(TokenPayload payload) {
        return generateConcreteToken(
                Objects.requireNonNull(payload, "payload cannot be null"),
                JsonTokenType.ACCESS
        );
    }

    public String generateRefreshToken(TokenPayload payload) {
        return generateConcreteToken(
                Objects.requireNonNull(payload, "payload cannot be null"),
                JsonTokenType.REFRESH
        );
    }

    private String generateConcreteToken(TokenPayload payload, JsonTokenType tokenType) {
        if(payload.getTokenType() != tokenType)
            throw new IllegalArgumentException("tokenType in payload is not corresponds with provided tokenType");

        int validityTimeSeconds = switch (tokenType) {
            case ACCESS -> validityTimeProperties.accessTokenValiditySeconds();
            case REFRESH -> validityTimeProperties.refreshTokenValiditySeconds();
        };
        return generate(payload, Duration.ofSeconds(validityTimeSeconds));
    }

    private String generate(TokenPayload payload, Duration validity) {
        if (validity.isNegative() || validity.isZero()) {
            throw new IllegalArgumentException("validity must be positive");
        }

        try {
            Map<String, Object> claims = objectMapper.convertValue(payload, Map.class);

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
}
