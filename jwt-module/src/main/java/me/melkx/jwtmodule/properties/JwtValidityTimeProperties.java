package me.melkx.jwtmodule.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

@ConfigurationProperties(prefix = "ecs.jwt")
public record JwtValidityTimeProperties(Integer accessTokenValiditySeconds, Integer refreshTokenValiditySeconds) {
    public JwtValidityTimeProperties {
        Objects.requireNonNull(accessTokenValiditySeconds, "accessTokenValiditySeconds cannot be null");
        Objects.requireNonNull(refreshTokenValiditySeconds, "refreshTokenValiditySeconds cannot be null");
    }
}
