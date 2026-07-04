package me.melkx.shared;

import java.time.LocalDateTime;

public record FormattedError(String error, Integer status, String message, String path, LocalDateTime timestamp) {
}
