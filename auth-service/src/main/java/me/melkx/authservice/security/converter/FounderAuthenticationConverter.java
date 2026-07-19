package me.melkx.authservice.security.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import me.melkx.authservice.security.CachedBodyHttpServletRequest;
import me.melkx.authservice.dto.FounderAuthenticationRequest;
import me.melkx.authservice.security.dto.FounderUnresolvedId;
import me.melkx.authservice.security.token.SignInAuthenticationToken;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.io.IOException;

public class FounderAuthenticationConverter implements AuthenticationConverter {
    private final ObjectMapper objectMapper;

    public FounderAuthenticationConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public @Nullable Authentication convert(HttpServletRequest request) {
        try {
            CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
            FounderAuthenticationRequest authReq = objectMapper.readValue(cachedRequest.getInputStream(), FounderAuthenticationRequest.class);
            return new SignInAuthenticationToken(new FounderUnresolvedId(authReq.email()), authReq.password());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
