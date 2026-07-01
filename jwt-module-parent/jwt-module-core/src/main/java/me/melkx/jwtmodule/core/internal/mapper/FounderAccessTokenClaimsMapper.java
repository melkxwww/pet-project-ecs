package me.melkx.jwtmodule.core.internal.mapper;

import io.jsonwebtoken.Claims;
import me.melkx.jwtmodule.core.internal.constant.JwtClaimFields;
import me.melkx.jwtmodule.core.api.dto.FounderAccessTokenClaims;
import me.melkx.jwtmodule.core.api.dto.JwtTokenType;
import me.melkx.jwtmodule.core.internal.util.UuidUtils;

public class FounderAccessTokenClaimsMapper extends AbstractJwtClaimsMapper<FounderAccessTokenClaims> {

    @Override
    protected Claims mapTokenClaimsInternal(FounderAccessTokenClaims tokenClaims, Claims claims) {
        claims.put(JwtClaimFields.ID, tokenClaims.id());
        claims.put(JwtClaimFields.TOKEN_TYPE, tokenClaims.tokenType());
        return claims;
    }

    @Override
    protected FounderAccessTokenClaims parseTokenClaimsInternal(Claims claims) {
        return new FounderAccessTokenClaims(
                UuidUtils.parseUUID(claims.get(JwtClaimFields.ID, String.class)),
                JwtTokenType.fromType(claims.get(JwtClaimFields.TOKEN_TYPE, String.class))
        );
    }
}
