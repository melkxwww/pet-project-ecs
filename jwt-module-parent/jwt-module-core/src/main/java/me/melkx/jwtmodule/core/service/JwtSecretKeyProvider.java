package me.melkx.jwtmodule.core.service;

import javax.crypto.SecretKey;

public interface JwtSecretKeyProvider {
    SecretKey getSecretKey();
}
