package me.melkx.authservice.service;

import me.melkx.jwtmodule.core.api.dto.RequiredFounderRefreshTokenClaims;
import me.melkx.jwtmodule.core.api.service.FounderRefreshTokenParser;
import me.melkx.jwtmodule.core.internal.constant.JwtValidityTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import me.melkx.jwtmodule.core.api.service.JwtParser;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class JwtRefreshService {
    private static final String GLOBAL_TOKEN_PREFIX = "token:";
    private static final String FOUNDER_BIND_TOKEN_PREFIX = "founder:tokens:";
    private static final String EMPLOYEE_BIND_TOKEN_PREFIX = "employee:tokens:";

    private final JwtParser jwtParser;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public JwtRefreshService(JwtParser jwtParser, StringRedisTemplate redisTemplate) {
        this.jwtParser = jwtParser;
        this.redisTemplate = redisTemplate;

    }

    public void registerFounderRefreshToken(String token) {
        FounderRefreshTokenParser parser = jwtParser.parseFounderRefreshToken(token);

        if(!parser.isCorrespondsType())
            throw new IllegalArgumentException("Type of provided token is not corresponds!");

        String refreshId = parser.extractRefreshId();
        String founderId = parser.extractId();

        redisTemplate.opsForValue().set(
                GLOBAL_TOKEN_PREFIX.concat(refreshId),
                founderId,
                Duration.ofSeconds(JwtValidityTimes.REFRESH_TOKEN_VALIDITY_SECONDS)
        );
        redisTemplate.opsForSet().add(FOUNDER_BIND_TOKEN_PREFIX.concat(founderId), refreshId);
    }

    public void recallFounderRefreshToken(String token) {
        FounderRefreshTokenParser parser = jwtParser.parseFounderRefreshToken(token);

        String refreshId = parser.extractRefreshId();

        redisTemplate.opsForValue().get(FOUNDER_BIND_TOKEN_PREFIX.concat(token));
    }
}
