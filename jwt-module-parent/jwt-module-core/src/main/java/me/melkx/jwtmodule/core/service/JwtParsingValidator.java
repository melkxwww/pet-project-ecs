package me.melkx.jwtmodule.core.service;

import me.melkx.jwtmodule.core.dto.TokenPayload;

public interface JwtParsingValidator {
    <T extends TokenPayload> boolean validate(T payload);
}
