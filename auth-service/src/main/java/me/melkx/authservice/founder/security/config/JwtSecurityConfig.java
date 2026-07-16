package me.melkx.authservice.founder.security.config;

import me.melkx.authmodule.strategy.jwt.service.JwtAuthenticationFactory;
import me.melkx.authservice.founder.security.factory.FounderJwtAuthenticationFactory;
import me.melkx.jwtmodule.core.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtSecurityConfig {
    @Bean
    public JwtAuthenticationFactory jwtAuthenticationFactory(JwtService jwtService) {
        return new FounderJwtAuthenticationFactory(jwtService);
    }
}
