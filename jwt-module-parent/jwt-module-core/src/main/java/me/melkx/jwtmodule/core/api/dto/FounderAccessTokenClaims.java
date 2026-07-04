package me.melkx.jwtmodule.core.api.dto;

import java.util.Objects;
import java.util.UUID;

public record FounderAccessTokenClaims(UUID id) {
    public FounderAccessTokenClaims {
        Objects.requireNonNull(id, "Id is required");
    }
}
