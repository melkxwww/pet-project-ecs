package me.melkx.authservice.service;

import me.melkx.authmodule.dto.PrincipalType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RedisTokenKeyNameRepository {
    private static final Map<PrincipalType, String> REDIS_PREFIXES = Map.of(
            PrincipalType.FOUNDER, "founders",
            PrincipalType.EMPLOYEE, "employees"
    );

    public static String prepareTokenKeyName(PrincipalType type, String principalId) {
        String prefix = REDIS_PREFIXES.get(type);
        if (prefix == null) {
            throw new IllegalArgumentException("No Redis prefix configured for principal type: " + type);
        }
        return prefix + ":" + principalId + ":tokens";
    }
}
