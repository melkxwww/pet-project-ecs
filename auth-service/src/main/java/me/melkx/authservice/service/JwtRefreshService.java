package me.melkx.authservice.service;

import me.melkx.authmodule.dto.PrincipalType;
import me.melkx.authservice.dto.CommonRefreshTokenPayload;
import me.melkx.jwtmodule.core.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtRefreshService {
    private static final String TOKEN_SHADOW_PREFIX = "tokens:shadow:";
    private static final String TOKEN_SHADOW_SEPARATOR = "|";

    private final JwtService jwtService;
    private final StringRedisTemplate redisTemplate;
    private final Map<PrincipalType, RedisKeyResolver> keyResolvers;

    @Autowired
    public JwtRefreshService(JwtService jwtService, StringRedisTemplate redisTemplate, List<RedisKeyResolver> keyResolvers) {
        this.jwtService = jwtService;
        this.redisTemplate = redisTemplate;
        this.keyResolvers = keyResolvers.stream()
                .collect(Collectors.toMap(RedisKeyResolver::getType, resolver -> resolver));
    }

    public void registerRefreshToken(String token) {
        Objects.requireNonNull(token, "Token cannot be null");
        RefreshInfo info = extractRefreshInfoFromToken(token);

        if (info.expiration().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token already expired");
        }

        Duration ttl = Duration.between(Instant.now(), info.expiration());
        String shadowKey = prepareShadowKeyName(info.refreshId(), info.localStorageKey());

        redisTemplate.opsForValue().set(shadowKey, "", ttl);
        redisTemplate.opsForSet().add(info.localStorageKey(), info.refreshId());
    }

    public void revokeRefreshToken(String token) {
        Objects.requireNonNull(token, "Token cannot be null");
        RefreshInfo info = extractRefreshInfoFromToken(token);

        redisTemplate.opsForSet().remove(info.localStorageKey(), info.refreshId());

        String shadowKey = prepareShadowKeyName(info.refreshId(), info.localStorageKey());
        redisTemplate.delete(shadowKey);
    }

    public void revokeAllRefreshTokensFromPrincipal(PrincipalType userType, String id) {
        String localStorageKey = keyResolvers.get(userType).resolveTokenKeyName(id);

        Set<String> refreshIds = redisTemplate.opsForSet().members(localStorageKey);

        if (refreshIds == null || refreshIds.isEmpty()) {
            return;
        }

        List<String> keysToDelete = refreshIds.stream()
                .map(refreshId -> prepareShadowKeyName(refreshId, localStorageKey))
                .collect(Collectors.toList());

        redisTemplate.delete(keysToDelete);
        redisTemplate.delete(localStorageKey);
    }

    private RefreshInfo extractRefreshInfoFromToken(String token) {
        CommonRefreshTokenPayload payload = jwtService.parseToken(token, CommonRefreshTokenPayload.class);

        String refreshId = payload.getJti().toString();
        String localStorageKey = keyResolvers.get(payload.getPrincipalType())
                .resolveTokenKeyName(payload.getSub().toString());

        return new RefreshInfo(refreshId, localStorageKey, payload.getExp());
    }

    @EventListener
    public void onRefreshTokenExpired(RedisKeyExpiredEvent<Object> event) {
        String expiredKey = new String(event.getSource());

        if (!expiredKey.startsWith(TOKEN_SHADOW_PREFIX)) {
            return;
        }

        String data = expiredKey.substring(TOKEN_SHADOW_PREFIX.length());
        int delimiterIndex = data.indexOf(TOKEN_SHADOW_SEPARATOR);

        if (delimiterIndex != -1) {
            String refreshId = data.substring(0, delimiterIndex);
            String localStorageKey = data.substring(delimiterIndex + 1);

            redisTemplate.opsForSet().remove(localStorageKey, refreshId);
        }
    }

    private static String prepareShadowKeyName(String refreshId, String localStorageKey) {
        return TOKEN_SHADOW_PREFIX
                .concat(refreshId)
                .concat(TOKEN_SHADOW_SEPARATOR)
                .concat(localStorageKey);
    }

    record RefreshInfo(String refreshId, String localStorageKey, Instant expiration) {
    }
}