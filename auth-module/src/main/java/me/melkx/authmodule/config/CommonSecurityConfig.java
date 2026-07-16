package me.melkx.authmodule.config;

import me.melkx.authmodule.dto.AuthenticationIgnoredUris;
import me.melkx.authmodule.service.AuthenticationEntryPointImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import tools.jackson.databind.ObjectMapper;

@AutoConfiguration
@EnableConfigurationProperties({AuthenticationIgnoredUris.class})
@EnableWebSecurity
@EnableMethodSecurity
public class CommonSecurityConfig {
    @Bean
    @ConditionalOnMissingBean(AuthenticationEntryPoint.class)
    public AuthenticationEntryPoint authenticationEntryPoint(ObjectMapper objectMapper) {
        return new AuthenticationEntryPointImpl(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
