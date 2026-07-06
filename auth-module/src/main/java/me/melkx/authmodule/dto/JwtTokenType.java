package me.melkx.authmodule.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.jsonwebtoken.Jwt;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public enum JwtTokenType {
    FOUNDER_ACCESS("founder_access"),
    FOUNDER_REFRESH("founder_refresh"),
    EMPLOYEE_ACCESS("employee_access"),
    EMPLOYEE_REFRESH("employee_refresh");

    private static final Map<String, JwtTokenType> correspondsMap;

    static {
        correspondsMap = Arrays.stream(JwtTokenType.values())
                .map(type -> Map.entry(type.getValue(), type))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private final String value;

    JwtTokenType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static JwtTokenType fromValue(String value) {
        return Optional.ofNullable(correspondsMap.get(value))
                .orElseThrow(() -> new IllegalArgumentException("Invalid token type: " + value));
    }
}
