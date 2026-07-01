package me.melkx.jwtmodule.core.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum JwtTokenType {
    FOUNDER_ACCESS_TOKEN("founder_access"),
    FOUNDER_REFRESH_TOKEN("founder_refresh"),
    EMPLOYEE_ACCESS_TOKEN("employee_access"),
    EMPLOYEE_REFRESH_TOKEN("employee_refresh"),;

    private final String type;

    private static final Map<String, JwtTokenType> BY_TYPE = Arrays.stream(values())
            .collect(Collectors.toMap(JwtTokenType::getType, e -> e));

    public static JwtTokenType fromType(String type) {
        return Optional.ofNullable(BY_TYPE.get(type))
                .orElseThrow(() -> new IllegalArgumentException("Invalid JwtTokenType: " + type));
    }
}
