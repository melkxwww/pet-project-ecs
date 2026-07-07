package me.melkx.authservice.service;

import me.melkx.authmodule.dto.PrincipalType;
import org.springframework.stereotype.Component;

@Component
public class FounderKeyResolver implements RedisKeyResolver {
    @Override
    public PrincipalType getType() {
        return PrincipalType.FOUNDER;
    }

    @Override
    public String resolveTokenKeyName(String userId) {
        return "founders:".concat(userId).concat(":tokens");
    }
}
