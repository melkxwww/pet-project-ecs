package me.melkx.authmodule.strategy.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import me.melkx.jwtmodule.core.dto.TokenPayload;

public interface TypedTokenPayload extends TokenPayload {
    @JsonProperty(required = true)
    JsonPrincipalType getPrincipalType();
}
