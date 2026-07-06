package me.melkx.jwtmodule.core.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.DeserializationException;
import io.jsonwebtoken.security.SignatureException;
import me.melkx.jwtmodule.core.api.dto.TokenType;
import me.melkx.jwtmodule.core.api.exception.JwtInternalException;
import me.melkx.jwtmodule.core.api.exception.JwtInvalidTokenException;
import me.melkx.jwtmodule.core.internal.constant.JwtValidityTimes;

import javax.crypto.SecretKey;
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
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public String generateToken(TokenPayload payload) {
        Map<String, Object> claims = objectMapper.convertValue(payload, new TypeReference<>() {});

        Date now = new Date();
        Date expiryDate = Date.from(Instant.now()
                .plusSeconds(
                        payload.getTokenType() == TokenType.ACCESS
                                ? JwtValidityTimes.ACCESS_TOKEN_VALIDITY_SECONDS
                                : JwtValidityTimes.REFRESH_TOKEN_VALIDITY_SECONDS
                ));

        try {
            return Jwts.builder()
                    .claims(claims)
                    .subject(payload.getUserId())
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
