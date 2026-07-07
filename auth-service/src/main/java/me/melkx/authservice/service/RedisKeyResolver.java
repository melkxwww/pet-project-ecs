package me.melkx.authservice.service;

import me.melkx.authmodule.dto.PrincipalType;

public interface RedisKeyResolver {
    public PrincipalType getType();
    public String resolveTokenKeyName(String userId);
}
