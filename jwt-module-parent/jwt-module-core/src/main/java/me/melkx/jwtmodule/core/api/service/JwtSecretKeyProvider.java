package me.melkx.jwtmodule.core.api.service;

import javax.crypto.SecretKey;

public interface JwtSecretKeyProvider {
    SecretKey getSecretKey();
}
