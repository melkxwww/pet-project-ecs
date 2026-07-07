package me.melkx.jwtmodule.spring;

import me.melkx.jwtmodule.core.service.JwtConfigurationProvider;
import me.melkx.jwtmodule.core.service.JwtSecretKeyProvider;
import me.melkx.jwtmodule.core.service.JwtService;
import me.melkx.jwtmodule.core.service.JwtServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties({JwtProperties.class})
public class JwtAutoConfig {
    @Bean
    @ConditionalOnMissingBean(JwtService.class)
    public JwtService jwtService(JwtProperties jwtProperties) {
        return new JwtServiceImpl(new JwtSecretKeyProviderImpl(), new JwtConfigurationProviderImpl(
                jwtProperties.getAccessTokenValiditySeconds(),
                jwtProperties.getRefreshTokenValiditySeconds()
        ));
    }
}
