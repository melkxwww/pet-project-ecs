package me.melkx.authmodule.api.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationContextLoadingFailedException extends AuthenticationException {
    public AuthenticationContextLoadingFailedException(String msg) {
        super(msg);
    }
}
