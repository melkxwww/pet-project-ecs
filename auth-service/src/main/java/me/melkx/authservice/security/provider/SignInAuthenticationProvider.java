package me.melkx.authservice.security.provider;

import me.melkx.authservice.security.dto.PublicIdPrincipal;
import me.melkx.authservice.security.token.SignInAuthenticationToken;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SignInAuthenticationProvider implements AuthenticationProvider {
    private final SignInPublicIdResolverCoordinator idResolver;
    private final SignInPasswordProvider passwordProvider;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SignInAuthenticationProvider(SignInPublicIdResolverCoordinator idResolver, SignInPasswordProvider passwordProvider, PasswordEncoder passwordEncoder) {
        this.idResolver = idResolver;
        this.passwordProvider = passwordProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SignInAuthenticationToken token;
        if (!(authentication instanceof SignInAuthenticationToken))
            throw new IllegalArgumentException("Invalid authentication provided!");

        token = (SignInAuthenticationToken) authentication;

        Object id = token.getPrincipal();
        String password = (String) token.getCredentials();

        if (id == null || password == null) {
            throw new BadCredentialsException("Id or password is null");
        }

        UUID concreteId = idResolver.resolvePublicId(id);

        if (!passwordEncoder.matches(
                passwordProvider.findPasswordHashByPublicId(concreteId), password)
        ) {
            throw new BadCredentialsException("Invalid password");
        }

        authentication = new SignInAuthenticationToken(new PublicIdPrincipal(concreteId));
        authentication.setAuthenticated(true);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SignInAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
