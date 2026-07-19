package me.melkx.authmodule.core.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.AuthenticationException;

public class AuthenticationContextLoadingFailedException extends AuthenticationException {
    public AuthenticationContextLoadingFailedException(String msg) {
        super(msg);
    }
}
