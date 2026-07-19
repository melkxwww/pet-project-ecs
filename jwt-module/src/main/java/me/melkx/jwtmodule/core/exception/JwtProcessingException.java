package me.melkx.jwtmodule.core.exception;

public final class JwtProcessingException extends RuntimeException {
    public JwtProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
