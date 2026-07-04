package me.melkx.jwtmodule.core.api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import me.melkx.jwtmodule.core.api.dto.JwtTokenType;
import me.melkx.jwtmodule.core.api.exception.JwtInternalException;
import me.melkx.jwtmodule.core.internal.constant.JwtClaimFields;
import me.melkx.jwtmodule.core.internal.constant.JwtValidityTimes;
import me.melkx.jwtmodule.core.api.dto.FounderAccessTokenClaims;
import me.melkx.jwtmodule.core.api.dto.FounderRefreshTokenClaims;
import me.melkx.jwtmodule.core.internal.mapper.FounderAccessTokenClaimsMapper;
import me.melkx.jwtmodule.core.internal.mapper.FounderRefreshTokenClaimsMapper;
import me.melkx.jwtmodule.core.internal.mapper.JwtClaimsMapper;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class JwtGenerator {
    private final JwtSecretKeyProvider secretKeyProvider;
    private final JwtClaimsMapper<FounderAccessTokenClaims> founderAccessClaimsMapper = new FounderAccessTokenClaimsMapper();
    private final JwtClaimsMapper<FounderRefreshTokenClaims> founderRefreshClaimsMapper = new FounderRefreshTokenClaimsMapper();

    public JwtGenerator(JwtSecretKeyProvider secretKeyProvider) {
        this.secretKeyProvider = secretKeyProvider;
    }

    public String generateFounderAccessToken(FounderAccessTokenClaims tokenClaims) {
        return generateAccessToken(founderAccessClaimsMapper.mapTokenClaims(tokenClaims), JwtTokenType.FOUNDER_ACCESS_TOKEN);
    }

    public String generateFounderRefreshToken(FounderRefreshTokenClaims tokenClaims) {
        return generateRefreshToken(founderRefreshClaimsMapper.mapTokenClaims(tokenClaims), JwtTokenType.FOUNDER_REFRESH_TOKEN);
    }

    private String generateAccessToken(Claims claims, JwtTokenType tokenType) {
        return generateToken(claims, tokenType, JwtValidityTimes.ACCESS_TOKEN_VALIDITY_SECONDS);
    }

    private String generateRefreshToken(Claims claims, JwtTokenType tokenType) {
        claims.put(JwtClaimFields.REFRESH_TOKEN_ID, UUID.randomUUID().toString());
        return generateToken(claims, tokenType, JwtValidityTimes.REFRESH_TOKEN_VALIDITY_SECONDS);
    }

    private String generateToken(Claims claims, JwtTokenType tokenType, int validitySeconds) {
        claims.put(JwtClaimFields.TOKEN_TYPE, tokenType.name());
        try {
            return Jwts.builder()
                    .signWith(secretKeyProvider.getSecretKey())
                    .claims(claims)
                    .issuedAt(new Date())
                    .expiration(Date.from(Instant.now().plusSeconds(validitySeconds)))
                    .compact();
        } catch (JwtException e) {
            throw new JwtInternalException(e.getMessage());
        }
    }
}
