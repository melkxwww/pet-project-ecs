package me.melkx.jwtmodule.core.exception;

public final class JwtInvalidTokenException extends RuntimeException {
    public JwtInvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
