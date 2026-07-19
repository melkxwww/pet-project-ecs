package me.melkx.authmodule.core.service;

import me.melkx.authmodule.core.dto.AuthenticationContext;
import me.melkx.authmodule.core.exception.AuthenticationContextLoadingFailedException;

import java.util.UUID;

public interface AuthenticationContextProvider {
    AuthenticationContext loadContextByPublicId(UUID publicId) throws AuthenticationContextLoadingFailedException;
}
