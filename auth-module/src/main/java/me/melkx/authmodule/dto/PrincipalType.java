package me.melkx.authmodule.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public enum PrincipalType {
    FOUNDER,
    EMPLOYEE,
    COMMON;

    @JsonValue
    public String getValue() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static PrincipalType fromValue(String value) {
        return switch (value) {
            case "founder" -> FOUNDER;
            case "employee" -> EMPLOYEE;
            case "common" -> COMMON;
            default -> throw new IllegalArgumentException("Invalid principal type value");
        };
    }
}
