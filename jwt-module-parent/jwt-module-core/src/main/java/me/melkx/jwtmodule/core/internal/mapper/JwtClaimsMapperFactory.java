package me.melkx.jwtmodule.core.internal.mapper;

import me.melkx.jwtmodule.core.api.dto.FounderAccessTokenClaims;
import me.melkx.jwtmodule.core.api.dto.FounderRefreshTokenClaims;

public class JwtClaimsMapperFactory {
    public static JwtClaimsMapper<FounderAccessTokenClaims> createFounderAccessClaimsMapper() {
        return new FounderAccessTokenClaimsMapper();
    }

    public static JwtClaimsMapper<FounderRefreshTokenClaims> createFounderRefreshClaimsMapper() {
        return new FounderRefreshTokenClaimsMapper();
    }
}
