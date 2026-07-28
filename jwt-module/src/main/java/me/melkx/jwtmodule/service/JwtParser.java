package me.melkx.jwtmodule.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import me.melkx.jwtmodule.dto.TokenPayload;

import javax.crypto.SecretKey;
import java.util.Objects;

@Slf4j
public class JwtParser {
    private final SecretKey secretKey;
    private final ObjectMapper objectMapper;

    public JwtParser(SecretKey secretKey, ObjectMapper objectMapper) {
        this.secretKey = Objects.requireNonNull(secretKey, "secretKey cannot be null");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper cannot be null");
    }

    public <T extends TokenPayload> ParseResult<T> parse(String token, Class<T> target) {
        log.debug("Parsing JWT token...");

        Objects.requireNonNull(token, "token cannot be null");
        Objects.requireNonNull(target, "target cannot be null");

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            log.debug("Parsing JWT successful!");

            return ParseResult.success(objectMapper.convertValue(claims, target));
        } catch (ExpiredJwtException e) {
            return ParseResult.failure("Token has expired");
        } catch (MalformedJwtException e) {
            return ParseResult.failure("Invalid token format");
        } catch (SignatureException e) {
            return ParseResult.failure("Invalid signature");
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token validation failed: {}", e.getMessage());

            return ParseResult.failure("Token validation failed");
        }
    }
}
