package me.melkx.authenticationmodule.config;

import me.melkx.authenticationmodule.dto.IgnoredUris;
import me.melkx.authenticationmodule.service.CommonJwtAuthenticator;
import me.melkx.authenticationmodule.service.FounderJwtAuthenticator;
import me.melkx.authenticationmodule.service.JwtAuthenticator;
import me.melkx.jwtmodule.core.api.service.JwtParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
public class AuthenticationConfig {
    private final JwtParser jwtParser;

    @Autowired
    public AuthenticationConfig(JwtParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    @Bean
    @ConditionalOnMissingBean(IgnoredUris.class)
    public IgnoredUris ignoredUris() {
        return new IgnoredUris();
    }

    @Bean
    @ConditionalOnMissingBean(JwtAuthenticator.class)
    public JwtAuthenticator jwtAuthenticator() {
        return new CommonJwtAuthenticator(jwtParser);
    }
}
