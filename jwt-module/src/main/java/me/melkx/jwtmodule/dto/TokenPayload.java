package me.melkx.jwtmodule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface TokenPayload {
    @JsonProperty(required = true, access = JsonProperty.Access.WRITE_ONLY)
    JsonTokenType getTokenType();
}
