package me.melkx.authmodule.jwt.provider;

import lombok.extern.slf4j.Slf4j;
import me.melkx.authmodule.api.dto.AuthenticationContext;
import me.melkx.authmodule.api.service.AuthenticationContextProvider;
import me.melkx.authmodule.jwt.dto.AccessTokenPayload;
import me.melkx.authmodule.jwt.token.JwtAuthenticationToken;
import me.melkx.jwtmodule.service.JwtParser;
import me.melkx.jwtmodule.service.ParseResult;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Objects;

@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final AuthenticationContextProvider contextProvider;
    private final JwtParser jwtParser;

    public JwtAuthenticationProvider(AuthenticationContextProvider contextProvider, JwtParser jwtParser) {
        this.contextProvider = Objects.requireNonNull(contextProvider, "contextProvider cannot be null");
        this.jwtParser = Objects.requireNonNull(jwtParser, "jwtParser cannot be null");
    }

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("Authentication JWT strategy started");

        Objects.requireNonNull(authentication, "authentication cannot be null");

        if (!(authentication instanceof JwtAuthenticationToken token))
            throw new AuthenticationServiceException("Invalid authentication provided!");

        String jwtToken = (String) token.getPrincipal();

        if (jwtToken == null)
            throw new AuthenticationServiceException("Invalid authentication token");

        log.debug("Authentication token correct");

        ParseResult<AccessTokenPayload> result = jwtParser.parse(jwtToken, AccessTokenPayload.class);
        if (!result.valid() || result.result() == null)
            throw new BadCredentialsException("Bad token provided: " + result.errorMessage());

        log.debug("Parsing correct");

        AuthenticationContext authorization = contextProvider.loadContextByPublicId(result.result().sub());
        authentication = new JwtAuthenticationToken(
                authorization.principal(),
                authorization.authorities()
        );
        authentication.setAuthenticated(true);

        log.debug("Authentication JWT strategy successful!");

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
