package me.melkx.jwtmodule.preparer;

import io.jsonwebtoken.security.WeakKeyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.crypto.SecretKey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtSecretKeyPreparerTest {

    @Test
    void prepareSecretKey_ShouldThrowException_WhenEncodedSecretKeyIsNull() {
        assertThatThrownBy(() -> JwtSecretKeyPreparer.prepareSecretKey(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("encodedSecretKey cannot be null");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "   ",
            "0123456789,./",
            "invalid!@#$",
            "MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MA=="
    })
    void prepareSecretKey_ShouldThrowException_WhenInvalidKeyProvided(String invalidKey) {
        assertThatThrownBy(() -> JwtSecretKeyPreparer.prepareSecretKey(invalidKey))
                .isInstanceOfAny(IllegalArgumentException.class, WeakKeyException.class);
    }

    @Test
    void prepareSecretKey_ShouldThrowException_WhenFinalEncodedSecretKeyLessThan32Bytes() {
        String smallEncodedSecretKey = "MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MA==";
        assertThatThrownBy(() -> JwtSecretKeyPreparer.prepareSecretKey(smallEncodedSecretKey))
                .isInstanceOf(WeakKeyException.class)
                .hasMessageContaining("The specified key byte array is");
    }

    @Test
    void prepareSecretKey_ShouldReturnSecretKey_WhenValidBase64Provided() {
        String validEncodedKey = "MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEy";

        SecretKey secretKey = JwtSecretKeyPreparer.prepareSecretKey(validEncodedKey);

        assertThat(secretKey)
                .isNotNull()
                .isInstanceOf(SecretKey.class);

        assertThat(secretKey.getAlgorithm())
                .isEqualTo("HmacSHA256");
    }

    @Test
    void prepareSecretKey_ShouldReturnSecretKey_WhenKeyIsExactly32Bytes() {
        String exactly32Bytes = "MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEy";

        SecretKey secretKey = JwtSecretKeyPreparer.prepareSecretKey(exactly32Bytes);

        assertThat(secretKey)
                .isNotNull()
                .hasFieldOrPropertyWithValue("algorithm", "HmacSHA256");
    }

    @Test
    void prepareSecretKey_ShouldReturnSecretKey_WhenKeyIsLongerThan32Bytes() {
        String exactly64Bytes = "MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDEy";

        SecretKey secretKey = JwtSecretKeyPreparer.prepareSecretKey(exactly64Bytes);

        assertThat(secretKey)
                .isNotNull()
                .hasFieldOrPropertyWithValue("algorithm", "HmacSHA384");
    }
}