package me.melkx.authmodule.strategy.jwt.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JsonPrincipalType {
    FOUNDER,
    EMPLOYEE;

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static JsonPrincipalType fromValue(String value) {
        return switch (value) {
            case "founder" -> FOUNDER;
            case "employee" -> EMPLOYEE;
            default -> throw new IllegalArgumentException("Invalid principal type value");
        };
    }
}
