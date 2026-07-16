package me.melkx.authservice.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app.jwt")
public class JwtValidityTimeProperties {
    private int accessValiditySeconds = 300;
    private int refreshValiditySeconds = 64800;
}
