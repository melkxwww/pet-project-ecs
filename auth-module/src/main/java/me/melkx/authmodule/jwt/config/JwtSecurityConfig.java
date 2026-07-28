package me.melkx.authmodule.jwt.config;

import me.melkx.authmodule.api.service.AuthenticationContextProvider;
import me.melkx.authmodule.filter.DelegatingAuthenticationFilter;
import me.melkx.authmodule.jwt.converter.JwtAuthenticationConverter;
import me.melkx.authmodule.jwt.provider.JwtAuthenticationProvider;
import me.melkx.jwtmodule.config.JwtAutoConfig;
import me.melkx.jwtmodule.service.JwtParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
import org.springframework.security.web.util.matcher.RequestMatcher;

@AutoConfiguration(after = JwtAutoConfig.class)
@ConditionalOnProperties({
        @ConditionalOnProperty(prefix = "ecs.security.auth-type", havingValue = "jwt"),
        @ConditionalOnProperty(prefix = "ecs.security.auth-type", matchIfMissing = true)
})
public class JwtSecurityConfig {
    @Bean("jwtAuthenticationConverter")
    public AuthenticationConverter jwtAuthenticationConverter() {
        return new JwtAuthenticationConverter();
    }

    @Bean("jwtAuthenticationProvider")
    public AuthenticationProvider jwtAuthenticationProvider(AuthenticationContextProvider contextProvider, JwtParser jwtParser) {
        return new JwtAuthenticationProvider(contextProvider, jwtParser);
    }

    @Bean("jwtAuthenticationFilter")
    public DelegatingAuthenticationFilter jwtAuthenticationFilter(
            @Qualifier("jwtAuthenticationManager") AuthenticationManager authenticationManager,
            @Qualifier("jwtAuthenticationConverter") AuthenticationConverter authenticationConverter,
            AuthenticationEntryPoint entryPoint,
            @Autowired(required = false)
            @Qualifier("jwtRequestMatcher") RequestMatcher requestMatcher) {
        return new DelegatingAuthenticationFilter(
                authenticationManager,
                authenticationConverter,
                entryPoint,
                requestMatcher
        );
    }


    @Bean("jwtSecurityFilterChain")
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
