package me.melkx.authmodule.config;

import me.melkx.authmodule.dto.IgnoredUris;
import me.melkx.jwtmodule.core.api.service.JwtParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {
    private final JwtParser jwtParser;

    @Autowired
    public AuthConfig(JwtParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    @Bean
    @ConditionalOnMissingBean(IgnoredUris.class)
    public IgnoredUris ignoredUris() {
        return new IgnoredUris();
    }
}
