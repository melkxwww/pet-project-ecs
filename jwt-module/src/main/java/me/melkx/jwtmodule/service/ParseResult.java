package me.melkx.jwtmodule.service;

import me.melkx.jwtmodule.dto.TokenPayload;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public record ParseResult<T extends TokenPayload>(@Nullable T result, boolean valid, @Nullable String errorMessage) {
    public static <T extends TokenPayload> ParseResult<T> success(T result) {
        Objects.requireNonNull(result, "result cannot be null");

        return new ParseResult<>(result, true, null);
    }

    public static <T extends TokenPayload> ParseResult<T> failure(String errorMessage) {
        Objects.requireNonNull(errorMessage, "errorMessage cannot be null");

        return new ParseResult<>(null, false, errorMessage);
    }
}
