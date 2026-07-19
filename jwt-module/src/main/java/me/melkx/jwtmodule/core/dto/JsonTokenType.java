package me.melkx.jwtmodule.core.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public enum JsonTokenType {
    ACCESS,
    REFRESH;

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static JsonTokenType fromValue(String value) {
        Objects.requireNonNull(value, "value cannot be null");

        return switch (value) {
            case "access" -> ACCESS;
            case "refresh" -> REFRESH;
            default -> throw new IllegalArgumentException("Invalid token type");
        };
    }
}
