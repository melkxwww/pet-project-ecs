package me.melkx.authmodule.strategy.jwt.config;

import me.melkx.authmodule.dto.AuthenticationIgnoredUris;
import me.melkx.authmodule.strategy.jwt.filter.JwtAuthenticationFilter;
import me.melkx.authmodule.strategy.jwt.service.JwtAuthenticationFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

@AutoConfiguration
public class JwtSecurityConfig {
    @Bean
    @ConditionalOnMissingBean(JwtAuthenticationFilter.class)
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtAuthenticationFactory factory,
            AuthenticationEntryPoint entryPoint,
            AuthenticationIgnoredUris ignoredUris
    ) {
        return new JwtAuthenticationFilter(factory, entryPoint, ignoredUris);
    }

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .securityContext(s -> s.requireExplicitSave(false))
                .authorizeHttpRequests(a ->
                        a.anyRequest().authenticated())
                .addFilterBefore(jwtFilter, AnonymousAuthenticationFilter.class);

        return http.build();
    }
}
