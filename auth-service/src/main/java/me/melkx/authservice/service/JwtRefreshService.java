package me.melkx.authservice.service;

import me.melkx.authmodule.dto.jwt.EmployeeRefreshTokenPayload;
import me.melkx.authmodule.dto.jwt.FounderRefreshTokenPayload;
import me.melkx.authmodule.service.JwtServiceFacade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class JwtRefreshService {
    private static final String TOKEN_PREFIX = "tokens:";

    private final JwtServiceFacade jwtService;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public JwtRefreshService(JwtServiceFacade jwtService, StringRedisTemplate redisTemplate) {
        this.jwtService = jwtService;
        this.redisTemplate = redisTemplate;

    }

    public void registerFounderRefreshToken(String token) {
        FounderRefreshTokenPayload payload = jwtService.parseToken(token, FounderRefreshTokenPayload.class);
        registerRefreshToken(
                prepareFounderTokenKeyName(payload.sub().toString()),
                payload.jti().toString(),
                payload.exp()
        );
    }

    public void recallFounderRefreshToken(String token) {
        FounderRefreshTokenPayload payload = jwtService.parseToken(token, FounderRefreshTokenPayload.class);
        recallRefreshToken(
                prepareFounderTokenKeyName(payload.sub().toString()),
                payload.jti().toString()
        );
    }

    public void registerEmployeeRefreshToken(String token) {
        EmployeeRefreshTokenPayload payload = jwtService.parseToken(token, EmployeeRefreshTokenPayload.class);
        registerRefreshToken(
                prepareFounderTokenKeyName(payload.sub().toString()),
                payload.jti().toString(),
                payload.exp()
        );
    }

    public void recallEmployeeRefreshToken(String token) {
        EmployeeRefreshTokenPayload payload = jwtService.parseToken(token, EmployeeRefreshTokenPayload.class);
        recallRefreshToken(
                prepareFounderTokenKeyName(payload.sub().toString()),
                payload.jti().toString()
        );
    }

    private void registerRefreshToken(String localStorageKey, String jti, Instant exp) {
        Duration ttl = Duration.between(Instant.now(), exp);
        redisTemplate.opsForValue().set(
                prepareTokenKeyName(jti),
                localStorageKey,
                ttl
        );
        redisTemplate.opsForSet().add(localStorageKey, jti);
    }

    public void recallRefreshToken(String localStorageKey, String jti) {
        redisTemplate.delete(prepareTokenKeyName(jti));
        redisTemplate.opsForSet().remove(
                localStorageKey,
                jti
        );
    }

    @EventListener
    public void onRefreshTokenExpired(RedisKeyExpiredEvent<Object> event) {
        String expiredKey = new String(event.getSource());

        if (!expiredKey.startsWith(TOKEN_PREFIX))
            return;

        Object expiredValue = event.getValue();
        if (expiredValue != null) {
            String refreshId = expiredKey.substring(TOKEN_PREFIX.length());
            redisTemplate.opsForSet().remove((String) expiredValue, refreshId);
            return;
        }

        throw new IllegalStateException("Failed to deserialize refresh token");
    }

    private static String prepareTokenKeyName(String refreshId) {
        return TOKEN_PREFIX.concat(refreshId);
    }

    private static String prepareFounderTokenKeyName(String founderId) {
        return "founders:".concat(founderId).concat(":tokens");
    }

    private static String prepareEmployeeTokenKeyName(String employeeId) {
        return "employees:".concat(employeeId).concat(":tokens");
    }
}
