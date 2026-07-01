package me.melkx.jwtmodule.spring;

import me.melkx.jwtmodule.core.api.service.JwtGenerator;
import me.melkx.jwtmodule.core.api.service.JwtParser;
import me.melkx.jwtmodule.core.api.service.JwtSecretKeyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    private final JwtSecretKeyProvider secretKeyProvider = new JwtSecretKeyProviderImpl();

    @Bean
    @ConditionalOnMissingBean(JwtGenerator.class)
    public JwtGenerator jwtGenerator() {
        return new JwtGenerator(secretKeyProvider);
    }

    @Bean
    @ConditionalOnMissingBean(JwtParser.class)
    public JwtParser jwtParser() {
        return new JwtParser(secretKeyProvider);
    }
}
