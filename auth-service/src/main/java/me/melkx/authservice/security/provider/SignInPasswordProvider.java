package me.melkx.authservice.security.provider;

import java.util.UUID;

public interface SignInPasswordProvider {
    String findPasswordHashByPublicId(UUID id);
}
