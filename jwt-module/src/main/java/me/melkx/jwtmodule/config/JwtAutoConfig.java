package me.melkx.jwtmodule.config;

import me.melkx.jwtmodule.core.service.JwtService;
import me.melkx.jwtmodule.provider.JwtSecretKeyProvider;

@AutoConfiguration
public class JwtAutoConfig {
    @Bean
    public JwtService jwtService() {
        return new JwtService(new JwtSecretKeyProvider().getSecretKey());
    }
}
