package me.melkx.jwtmodule.preparer;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Objects;

@Slf4j
public class JwtSecretKeyPreparer {
    public static SecretKey prepareSecretKey(String encodedSecretKey) {
        log.debug("Preparing secret key...");

        Objects.requireNonNull(encodedSecretKey, "encodedSecretKey cannot be null");

        return Keys
                .hmacShaKeyFor(Base64.getDecoder().decode(encodedSecretKey));
    }
}
