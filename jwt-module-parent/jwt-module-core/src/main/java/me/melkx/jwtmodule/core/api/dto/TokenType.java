package me.melkx.jwtmodule.core.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TokenType {
    REFRESH,
    ACCESS;

    private final String value;

    TokenType() {
        this.value = name().toLowerCase();
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TokenType fromValue(String value) {
        return switch (value) {
            case "refresh" -> REFRESH;
            case "access" -> ACCESS;
            default -> throw new IllegalArgumentException("Invalid token type");
        };
    }
}
