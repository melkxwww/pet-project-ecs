package me.melkx.jwtmodule.preparer;

import io.jsonwebtoken.security.WeakKeyException;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.assertj.core.api.Assertions.*;

class JwtSecretKeyPreparerTest {
    @Test
    void prepareSecretKey_ShouldThrowException_WhenEncodedSecretKeyIsNull() {
        assertThatThrownBy(() -> JwtSecretKeyPreparer.prepareSecretKey(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("encodedSecretKey cannot be null");
    }

    @Test
    void prepareSecretKey_ShouldThrowException_WhenEncodedSecretKeyIsNotBase64Format() {
        String invalidEncodedSecretKey = "0123456789,./";
        assertThatThrownBy(() -> JwtSecretKeyPreparer.prepareSecretKey(invalidEncodedSecretKey))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void prepareSecretKey_ShouldThrowException_WhenFinalEncodedSecretKeyLessThan32Bytes() {
        String smallEncodedSecretKey = "MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MA==";
        assertThatThrownBy(() -> JwtSecretKeyPreparer.prepareSecretKey(smallEncodedSecretKey))
                .isInstanceOf(WeakKeyException.class);
    }

    @Test
    void prepareSecretKey_ShouldReturnSecretKey() {
        String encodedSecretKey = "MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEy";

        SecretKey secretKey = JwtSecretKeyPreparer.prepareSecretKey(encodedSecretKey);

        assertThat(secretKey)
                .isNotNull();
    }
}