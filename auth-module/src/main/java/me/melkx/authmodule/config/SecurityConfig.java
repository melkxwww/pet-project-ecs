package me.melkx.authmodule.config;

import me.melkx.authmodule.filter.FormattedAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;

@AutoConfiguration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint(ObjectMapper objectMapper) {
        return new FormattedAuthenticationEntryPoint(objectMapper);
    }
}
