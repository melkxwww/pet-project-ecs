package me.melkx.authservice.security.provider;

import java.util.UUID;

public interface SignInPublicIdResolver {
    UUID resolvePublicId(Object unresolvedId);
    boolean supports(Class<?> unresolvedId);
}
