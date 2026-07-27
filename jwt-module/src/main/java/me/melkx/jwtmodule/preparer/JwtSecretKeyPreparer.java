package me.melkx.jwtmodule.preparer;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Objects;

public class JwtSecretKeyPreparer {
    public static SecretKey prepareSecretKey(String encodedSecretKey) {
        Objects.requireNonNull(encodedSecretKey, "encodedSecretKey cannot be null");

        return Keys
                .hmacShaKeyFor(Base64.getDecoder().decode(encodedSecretKey));
    }
}
