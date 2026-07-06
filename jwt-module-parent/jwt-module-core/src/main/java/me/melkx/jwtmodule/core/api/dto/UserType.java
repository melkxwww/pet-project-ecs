package me.melkx.jwtmodule.core.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType {
    FOUNDER,
    EMPLOYEE;

    private final String value;

    UserType() {
        this.value = name().toLowerCase();
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static UserType fromValue(String value) {
        return switch (value) {
            case "founder" -> FOUNDER;
            case "employee" -> EMPLOYEE;
            default -> throw new IllegalArgumentException("Invalid user type");
        };
    }
}
