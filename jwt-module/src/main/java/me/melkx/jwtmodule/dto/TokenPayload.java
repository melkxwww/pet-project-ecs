package me.melkx.jwtmodule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jspecify.annotations.NonNull;

public interface TokenPayload {
    @JsonProperty(required = true, access = JsonProperty.Access.WRITE_ONLY)
    JsonTokenType getTokenType();
}
