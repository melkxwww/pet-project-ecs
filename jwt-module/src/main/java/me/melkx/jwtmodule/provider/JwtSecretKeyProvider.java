package me.melkx.jwtmodule.provider;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import me.melkx.jwtmodule.core.exception.JwtProcessingException;

import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtSecretKeyProvider {
    private static final String JWT_SECRET_KEY_ENV_NAME = "JWT_SECRET_KEY";
    private final SecretKey secretKey;

    public JwtSecretKeyProvider() {
        try {
            String rawSecretKey = System.getenv(JWT_SECRET_KEY_ENV_NAME);
            secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(rawSecretKey));
        } catch (WeakKeyException e) {
            throw new IllegalStateException("Invalid secret key format");
        }
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}
