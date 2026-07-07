package me.melkx.jwtmodule.spring;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    private int accessTokenValiditySeconds = 300;
    private int refreshTokenValiditySeconds = 604800;
}
