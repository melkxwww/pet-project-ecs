package me.melkx.jwtmodule.core.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TokenType {
    ACCESS,
    REFRESH;

    private String  value;

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static TokenType fromValue(String value) {
        return switch (value) {
            case "access" -> ACCESS;
            case "refresh" -> REFRESH;
            default -> throw new IllegalArgumentException("Invalid token type");
        };
    }
}
