package me.melkx.authmodule.config;

import me.melkx.authmodule.dto.IgnoredUris;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthConfig {
    @Bean
    @ConditionalOnMissingBean(IgnoredUris.class)
    public IgnoredUris ignoredUris() {
        return new IgnoredUris();
    }
}
