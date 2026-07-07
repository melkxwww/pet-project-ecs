package me.melkx.jwtmodule.spring;

import me.melkx.jwtmodule.core.service.JwtConfiguration;
import me.melkx.jwtmodule.core.service.JwtConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

public class JwtConfigurationProviderImpl implements JwtConfigurationProvider {
    private final JwtConfiguration jwtConfiguration;

    public JwtConfigurationProviderImpl(long accessValidity,
                                        long refreshValidity) {
        this.jwtConfiguration = new JwtConfiguration(Duration.ofSeconds(accessValidity), Duration.ofSeconds(refreshValidity));
    }

    @Override
    public JwtConfiguration getJwtConfiguration() {
        return jwtConfiguration;
    }
}
