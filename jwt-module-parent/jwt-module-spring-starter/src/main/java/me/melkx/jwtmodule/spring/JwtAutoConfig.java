package me.melkx.jwtmodule.spring;

import me.melkx.jwtmodule.core.service.JwtService;
import me.melkx.jwtmodule.core.service.JwtServiceImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class JwtAutoConfig {
    @Bean
    @ConditionalOnMissingBean(JwtSecretKeyProvider.class)
    public JwtSecretKeyProvider jwtSecretKeyProvider() {
        return new JwtSecretKeyProviderImpl();
    }

    @Bean
    @ConditionalOnMissingBean(JwtService.class)
    public JwtService jwtService(JwtSecretKeyProvider secretKeyProvider) {
        return new JwtServiceImpl(secretKeyProvider.getSecretKey());
    }
}
