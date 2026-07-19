package me.melkx.authmodule.jwt.provider;

import me.melkx.authmodule.jwt.dto.AccessTokenPayload;
import me.melkx.authmodule.core.dto.AuthenticationContext;
import me.melkx.authmodule.core.service.AuthenticationContextProvider;
import me.melkx.authmodule.jwt.token.JwtAuthenticationToken;
import me.melkx.jwtmodule.core.service.JwtService;
import me.melkx.jwtmodule.core.service.ValidationResult;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final AuthenticationContextProvider contextProvider;
    private final JwtService jwtService;

    public JwtAuthenticationProvider(AuthenticationContextProvider contextProvider, JwtService jwtService) {
        this.contextProvider = contextProvider;
        this.jwtService = jwtService;
    }

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken token;
        if (!(authentication instanceof JwtAuthenticationToken))
            throw new IllegalArgumentException("Invalid authentication provided!");

        token = (JwtAuthenticationToken) authentication;

        String jwtToken = (String) token.getPrincipal();

        ValidationResult result = jwtService.validate(jwtToken);
        if (!result.valid())
            throw new BadCredentialsException(result.errorMessage());

        AccessTokenPayload payload = jwtService.read(jwtToken, AccessTokenPayload.class);

        AuthenticationContext authorization = contextProvider.loadContextByPublicId(payload.id());
        authentication = new JwtAuthenticationToken(
                authorization.principal(),
                authorization.authorities()
        );
        authentication.setAuthenticated(true);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
