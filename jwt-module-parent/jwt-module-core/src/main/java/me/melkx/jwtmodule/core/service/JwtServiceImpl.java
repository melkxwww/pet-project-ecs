package me.melkx.jwtmodule.core.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.DeserializationException;
import io.jsonwebtoken.security.SignatureException;
import me.melkx.jwtmodule.core.exception.JwtInternalException;
import me.melkx.jwtmodule.core.exception.JwtInvalidTokenException;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

public class JwtServiceImpl implements JwtService {
    private final SecretKey secretKey;
    private final ObjectMapper objectMapper;

    public JwtServiceImpl(JwtSecretKeyProvider secretKeyProvider) {
        this.secretKey = secretKeyProvider.getSecretKey();
        this.objectMapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
    }

    @Override
    public String generateToken(TokenPayload payload, Duration validity) {
        Map<String, Object> claims = objectMapper.convertValue(payload, new TypeReference<>() {
        });

        Date now = new Date();
        Date expiryDate = Date.from(Instant.now().plus(validity));

        try {
            return Jwts.builder()
                    .claims(claims)
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(secretKey)
                    .compact();
        } catch (JwtException e) {
            throw new JwtInternalException(e.getMessage());
        }
    }

    @Override
    public <T extends TokenPayload> T parseToken(String token, Class<T> targetClass) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return objectMapper.convertValue(claims, targetClass);
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
}
