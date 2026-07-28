package me.melkx.jwtmodule.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import me.melkx.jwtmodule.preparer.JwtSecretKeyPreparer;
import me.melkx.jwtmodule.properties.JwtValidityTimeProperties;
import me.melkx.jwtmodule.service.JwtGenerator;
import me.melkx.jwtmodule.service.JwtParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.crypto.SecretKey;

@AutoConfiguration
@ConditionalOnProperties({
        @ConditionalOnProperty(prefix = "ecs.jwt.enabled", havingValue = "true"),
        @ConditionalOnProperty(prefix = "ecs.jwt.enabled", matchIfMissing = true)
})
@EnableConfigurationProperties(JwtValidityTimeProperties.class)
public class JwtAutoConfig {
    private final SecretKey secretKey;

    @Autowired
    public JwtAutoConfig(@Value("${ecs.jwt.secret-key-env}") String rawSecretKey) {
        this.secretKey = JwtSecretKeyPreparer.prepareSecretKey(rawSecretKey);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
    }

    @Bean
    public JwtGenerator jwtGenerator(JwtValidityTimeProperties validityTimeProperties) {
        return new JwtGenerator(secretKey, validityTimeProperties, objectMapper());
    }

    @Bean
    public JwtParser jwtParser() {
        return new JwtParser(secretKey, objectMapper());
    }
}
