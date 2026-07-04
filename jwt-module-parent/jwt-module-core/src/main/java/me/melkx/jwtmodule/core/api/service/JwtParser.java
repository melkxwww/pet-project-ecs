package me.melkx.jwtmodule.core.api.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.DeserializationException;
import io.jsonwebtoken.security.SignatureException;
import me.melkx.jwtmodule.core.api.dto.JwtTokenType;
import me.melkx.jwtmodule.core.api.dto.FounderAccessTokenClaims;
import me.melkx.jwtmodule.core.api.dto.FounderRefreshTokenClaims;
import me.melkx.jwtmodule.core.api.exception.JwtInternalException;
import me.melkx.jwtmodule.core.api.exception.JwtInvalidTokenException;
import me.melkx.jwtmodule.core.internal.constant.JwtClaimFields;
import me.melkx.jwtmodule.core.internal.mapper.FounderAccessTokenClaimsMapper;
import me.melkx.jwtmodule.core.internal.mapper.FounderRefreshTokenClaimsMapper;
import me.melkx.jwtmodule.core.internal.mapper.JwtClaimsMapper;

public class JwtParser {
    private final JwtSecretKeyProvider secretKeyProvider;
    private final JwtClaimsMapper<FounderAccessTokenClaims> founderAccessClaimsMapper = new FounderAccessTokenClaimsMapper();
    private final JwtClaimsMapper<FounderRefreshTokenClaims> founderRefreshClaimsMapper = new FounderRefreshTokenClaimsMapper();

    public JwtParser(JwtSecretKeyProvider secretKeyProvider) {
        this.secretKeyProvider = secretKeyProvider;
    }

    public FounderAccessTokenClaims parseFounderAccessToken(String token) {
        return founderAccessClaimsMapper.parseTokenClaims(parseToken(token));
    }

    public FounderRefreshTokenClaims parseFounderRefreshToken(String token) {
        return founderRefreshClaimsMapper.parseTokenClaims(parseToken(token));
    }

    public JwtTokenType parseJwtTokenType(String token) {
        return JwtTokenType.fromType(parseToken(token)
                .get(JwtClaimFields.TOKEN_TYPE, String.class));
    }

    private Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKeyProvider.getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (MalformedJwtException e) {
            throw new JwtInvalidTokenException("Invalid format");
        } catch (ExpiredJwtException e) {
            throw new JwtInvalidTokenException("Expired token");
        } catch (SignatureException e) {
            throw new JwtInvalidTokenException("Invalid signature");
        } catch (DeserializationException e) {
            throw new JwtInvalidTokenException("Deserialization failed");
        } catch (JwtException e) {
            throw new JwtInternalException(e.getMessage());
        }
    }
}
