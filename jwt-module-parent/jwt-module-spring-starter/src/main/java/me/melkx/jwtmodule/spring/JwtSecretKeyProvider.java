package me.melkx.jwtmodule.spring;

import javax.crypto.SecretKey;

public interface JwtSecretKeyProvider {
    SecretKey getSecretKey();
}
