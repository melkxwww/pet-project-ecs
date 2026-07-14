package me.melkx.jwtmodule.spring;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import me.melkx.jwtmodule.core.exception.JwtInternalException;

import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtSecretKeyProviderImpl implements JwtSecretKeyProvider {
    private static final String JWT_SECRET_KEY_ENV_NAME = "JWT_SECRET_KEY";
    private final SecretKey secretKey;

    public JwtSecretKeyProviderImpl() {
        try {
            String rawSecretKey = System.getenv(JWT_SECRET_KEY_ENV_NAME);
            secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(rawSecretKey));
        } catch (WeakKeyException e) {
            throw new JwtInternalException("Failed to create secret key from env");
        } catch (RuntimeException e) {
            throw new JwtInternalException("Jwt secret key not found in env");
        }
    }

    @Override
    public SecretKey getSecretKey() {
        return secretKey;
    }
}
