package me.melkx.jwtmodule.core.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.DeserializationException;
import io.jsonwebtoken.security.SignatureException;
import me.melkx.jwtmodule.core.dto.TokenPayload;
import me.melkx.jwtmodule.core.exception.JwtInternalException;
import me.melkx.jwtmodule.core.exception.JwtInvalidTokenException;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class JwtServiceImpl implements JwtService {
    private static final ObjectMapper OBJECT_MAPPER;

    private final SecretKey secretKey;

    static {
        OBJECT_MAPPER = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
    }

    public JwtServiceImpl(SecretKey secretKey) {
        Objects.requireNonNull(secretKey, "Secret key cannot be null");

        this.secretKey = secretKey;
    }

    @Override
    public String generateToken(TokenPayload payload, Duration validity) {
        Objects.requireNonNull(payload, "Payload cannot be null");

        Map<String, Object> claims = OBJECT_MAPPER.convertValue(payload, Map.class);

        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date expiryDate = Date.from(now.plus(validity));

        try {
            return Jwts.builder()
                    .claims(claims)
                    .issuedAt(issuedAt)
                    .expiration(expiryDate)
                    .signWith(secretKey)
                    .compact();
        } catch (JwtException e) {
            throw new JwtInternalException(e.getMessage());
        }
    }

    @Override
    public <T extends TokenPayload> T parseToken(String token, Class<T> targetClass) {
        Objects.requireNonNull(token, "Token cannot be null");

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return OBJECT_MAPPER.convertValue(claims, targetClass);
        } catch (MalformedJwtException e) {
            throw new JwtInvalidTokenException("Invalid format");
        } catch (ExpiredJwtException e) {
            throw new JwtInvalidTokenException("Expired token");
        } catch (SignatureException e) {
            throw new JwtInvalidTokenException("Invalid signature");
        } catch (DeserializationException e) {
            throw new JwtInvalidTokenException("Deserialization failed");
        } catch (JwtException e) {
            throw new JwtInternalException(e.getMessage());
        }
    }

    @Override
    public <T extends TokenPayload> T readTokenClaims(String token, Class<T> targetClass) {
        Objects.requireNonNull(token, "Token cannot be null");

        try {
            String unsecuredToken = stripSignature(token);

            Claims claims = Jwts.parser()
                    .build()
                    .parseUnsecuredClaims(unsecuredToken)
                    .getPayload();

            return OBJECT_MAPPER.convertValue(claims, targetClass);
        } catch (Exception e) {
            throw new JwtInvalidTokenException("Invalid token format");
        }
    }

    private String stripSignature(String token) {
        int lastDot = token.lastIndexOf('.');
        if (lastDot <= 0) {
            return token;
        }

        return token.substring(0, lastDot + 1);
    }
}
