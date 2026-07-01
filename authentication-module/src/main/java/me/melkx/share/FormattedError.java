package me.melkx.share;

import java.time.LocalDateTime;

public record FormattedError(String error, Integer status, String message, String path, LocalDateTime timestamp) {
}

