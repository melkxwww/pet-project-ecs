package me.melkx.jwtmodule.core.exception;

public class JwtInvalidTokenException extends RuntimeException {
    public JwtInvalidTokenException(String message) {
        super(message);
    }
}
