package me.melkx.authmodule.strategy.jwt.config;

import me.melkx.authmodule.strategy.jwt.service.CommonPrincipalFactory;
import me.melkx.authmodule.strategy.jwt.service.EmployeePrincipalFactory;
import me.melkx.authmodule.strategy.jwt.service.FounderPrincipalFactory;
import me.melkx.authmodule.strategy.jwt.service.JwtPrincipalFactory;
import me.melkx.jwtmodule.core.service.JwtService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
public class JwtAutoConfig {
    @Bean
    @ConditionalOnMissingBean(JwtPrincipalFactory.class)
    public JwtPrincipalFactory commonPrincipalFactory(JwtService jwtService, List<JwtPrincipalFactory> factories) {
        return new CommonPrincipalFactory(jwtService, factories);
    }
}
