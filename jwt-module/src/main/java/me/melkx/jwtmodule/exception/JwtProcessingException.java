package me.melkx.jwtmodule.exception;

public final class JwtProcessingException extends RuntimeException {
    public JwtProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
