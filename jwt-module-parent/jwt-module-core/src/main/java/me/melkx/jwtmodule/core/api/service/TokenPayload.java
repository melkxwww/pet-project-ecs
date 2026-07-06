package me.melkx.jwtmodule.core.api.service;

import me.melkx.jwtmodule.core.api.dto.TokenType;
import me.melkx.jwtmodule.core.api.dto.UserType;

import java.util.Date;

public interface TokenPayload {
    TokenType getTokenType();
    UserType getUserType();
    String getUserId();
}
