package me.melkx.authmodule.api.service;

import me.melkx.authmodule.api.dto.AuthenticationContext;
import me.melkx.authmodule.api.exception.AuthenticationContextLoadingFailedException;
import org.jspecify.annotations.NonNull;

import java.util.UUID;

public interface AuthenticationContextProvider {
    AuthenticationContext loadContextByPublicId(UUID publicId) throws AuthenticationContextLoadingFailedException;
}
