package me.melkx.jwtmodule.core.api.dto;

import java.util.Objects;
import java.util.UUID;

public record FounderRefreshTokenClaims(UUID id) {
    public FounderRefreshTokenClaims {
        Objects.requireNonNull(id, "Id cannot be null");
    }
}
