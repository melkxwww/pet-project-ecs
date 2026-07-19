package me.melkx.authmodule.core.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.AuthenticationException;

public class InternalAuthenticationException extends AuthenticationException {
    public InternalAuthenticationException(String msg) {
        super(msg);
    }
}
