package me.melkx.authmodule.jwt.config;

import me.melkx.authmodule.jwt.converter.JwtAuthenticationConverter;
import me.melkx.authmodule.jwt.provider.JwtAuthenticationProvider;
import me.melkx.authmodule.core.filter.DelegatingAuthenticationFilter;
import me.melkx.authmodule.core.service.AuthenticationContextProvider;
import me.melkx.jwtmodule.core.service.JwtService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;

@AutoConfiguration
public class JwtSecurityConfig {
    @Bean
    public AuthenticationConverter jwtAuthenticationConverter() {
        return new JwtAuthenticationConverter();
    }

    @Bean
    public AuthenticationProvider jwtAuthenticationProvider(AuthenticationContextProvider contextProvider, JwtService jwtService) {
        return new JwtAuthenticationProvider(contextProvider, jwtService);
    }

    @Bean
    public DelegatingAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                                                  AuthenticationConverter jwtAuthenticationConverter,
                                                                  AuthenticationEntryPoint entryPoint) {
        return new DelegatingAuthenticationFilter(authenticationManager, jwtAuthenticationConverter, entryPoint, null);
    }

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    public SecurityFilterChain filterChain(HttpSecurity http, DelegatingAuthenticationFilter jwtFilter) throws Exception {
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
