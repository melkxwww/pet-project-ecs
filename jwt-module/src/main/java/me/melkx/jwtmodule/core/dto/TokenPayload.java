package me.melkx.jwtmodule.core.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface TokenPayload {
    @JsonProperty(required = true)
    JsonTokenType getTokenType();
}
