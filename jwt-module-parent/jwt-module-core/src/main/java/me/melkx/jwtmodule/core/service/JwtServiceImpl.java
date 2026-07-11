package me.melkx.jwtmodule.core.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.DeserializationException;
import io.jsonwebtoken.security.SignatureException;
import me.melkx.jwtmodule.core.dto.TokenPayload;
import me.melkx.jwtmodule.core.dto.TokenType;
import me.melkx.jwtmodule.core.exception.JwtInternalException;
import me.melkx.jwtmodule.core.exception.JwtInvalidTokenException;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class JwtServiceImpl implements JwtService {
    private final SecretKey secretKey;
    private final ObjectMapper objectMapper;
    private final JwtConfiguration configuration;
    private final JwtParsingValidator validator;

    public JwtServiceImpl(JwtSecretKeyProvider secretKeyProvider, JwtConfigurationProvider configurationProvider, JwtParsingValidator validator) {
        this.secretKey = secretKeyProvider.getSecretKey();
        this.configuration = configurationProvider.getJwtConfiguration();
        this.objectMapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
        this.validator = validator;
    }

    @Override
    public String generateAccessToken(TokenPayload payload) {
        if(payload.getTokenType() != TokenType.ACCESS)
            throw new IllegalArgumentException("Invalid token type");

        return generateToken(payload, configuration.accessTokenValidity());
    }

    @Override
    public String generateRefreshToken(TokenPayload payload) {
        if(payload.getTokenType() != TokenType.REFRESH)
            throw new IllegalArgumentException("Invalid token type");

        return generateToken(payload, configuration.refreshTokenValidity());
    }

    @Override
    public String generateToken(TokenPayload payload, Duration validity) {
        Objects.requireNonNull(payload, "Payload cannot be null");

        Map<String, Object> claims = objectMapper.convertValue(payload, Map.class);

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

            T payload = objectMapper.convertValue(claims, targetClass);

            if(!validator.validate(payload))
                throw new JwtInvalidTokenException("Invalid token provided");

            return payload;
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

            return objectMapper.convertValue(claims, targetClass);
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
