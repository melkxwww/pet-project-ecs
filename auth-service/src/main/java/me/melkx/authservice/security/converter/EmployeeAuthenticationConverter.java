package me.melkx.authservice.security.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import me.melkx.authservice.security.CachedBodyHttpServletRequest;
import me.melkx.authservice.dto.EmployeeAuthenticationRequest;
import me.melkx.authservice.security.dto.EmployeeUnresolvedId;
import me.melkx.authservice.security.token.SignInAuthenticationToken;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;

import java.io.IOException;

public class EmployeeAuthenticationConverter implements AuthenticationConverter {
    private final ObjectMapper objectMapper;

    public EmployeeAuthenticationConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public @Nullable Authentication convert(HttpServletRequest request) {
        try {
            CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
            EmployeeAuthenticationRequest authReq = objectMapper.readValue(cachedRequest.getInputStream(), EmployeeAuthenticationRequest.class);
            return new SignInAuthenticationToken(new EmployeeUnresolvedId(authReq.email(), authReq.companyCode()), authReq.password());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
