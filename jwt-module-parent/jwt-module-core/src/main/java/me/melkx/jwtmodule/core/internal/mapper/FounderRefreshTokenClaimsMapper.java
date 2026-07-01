package me.melkx.jwtmodule.core.internal.mapper;

import io.jsonwebtoken.Claims;
import me.melkx.jwtmodule.core.internal.constant.JwtClaimFields;
import me.melkx.jwtmodule.core.api.dto.FounderRefreshTokenClaims;
import me.melkx.jwtmodule.core.api.dto.JwtTokenType;
import me.melkx.jwtmodule.core.internal.util.UuidUtils;

public class FounderRefreshTokenClaimsMapper extends AbstractJwtClaimsMapper<FounderRefreshTokenClaims> {
    @Override
    protected Claims mapTokenClaimsInternal(FounderRefreshTokenClaims tokenClaims, Claims claims) {
        claims.put(JwtClaimFields.ID, tokenClaims.id());
        claims.put(JwtClaimFields.TOKEN_TYPE, tokenClaims.tokenType());
        return claims;
    }

    @Override
    protected FounderRefreshTokenClaims parseTokenClaimsInternal(Claims claims) {
        return new FounderRefreshTokenClaims(
                UuidUtils.parseUUID(claims.get(JwtClaimFields.ID, String.class)),
                JwtTokenType.fromType(claims.get(JwtClaimFields.TOKEN_TYPE, String.class))
        );
    }
}
