package me.melkx.authmodule.strategy.jwt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import me.melkx.authmodule.dto.PrincipalType;
import me.melkx.jwtmodule.core.service.TokenPayload;

public interface TypedTokenPayload extends TokenPayload {
    @JsonProperty
    PrincipalType getPrincipalType();
}
