package me.melkx.jwtmodule.spring;

import me.melkx.jwtmodule.core.service.JwtSecretKeyProvider;
import me.melkx.jwtmodule.core.service.JwtService;
import me.melkx.jwtmodule.core.service.JwtServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    private final JwtSecretKeyProvider secretKeyProvider = new JwtSecretKeyProviderImpl();

    @Bean
    @ConditionalOnMissingBean(JwtService.class)
    public JwtService jwtService() {
        return new JwtServiceImpl(secretKeyProvider);
    }
}
