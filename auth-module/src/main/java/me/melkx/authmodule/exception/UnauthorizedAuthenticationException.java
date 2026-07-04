package me.melkx.authmodule.exception;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedAuthenticationException extends AuthenticationException {
    public UnauthorizedAuthenticationException(String message) {
        super(message);
    }
}
