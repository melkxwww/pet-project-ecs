package me.melkx.authenticationmodule.exception;

import org.springframework.security.core.AuthenticationException;

public class InternalAuthenticationException extends AuthenticationException {
    public InternalAuthenticationException(String message) {
        super(message);
    }
}
