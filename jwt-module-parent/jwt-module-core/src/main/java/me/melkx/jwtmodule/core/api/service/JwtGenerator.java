package me.melkx.jwtmodule.core.api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import me.melkx.jwtmodule.core.api.exception.JwtInternalException;
import me.melkx.jwtmodule.core.internal.constant.JwtClaimFields;
import me.melkx.jwtmodule.core.internal.constant.JwtValidityTimes;
import me.melkx.jwtmodule.core.api.dto.FounderAccessTokenClaims;
import me.melkx.jwtmodule.core.api.dto.FounderRefreshTokenClaims;
import me.melkx.jwtmodule.core.internal.mapper.JwtClaimsMapper;
import me.melkx.jwtmodule.core.internal.mapper.JwtClaimsMapperFactory;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class JwtGenerator {
    private final JwtSecretKeyProvider secretKeyProvider;
    private final JwtClaimsMapper<FounderAccessTokenClaims> founderAccessClaimsMapper = JwtClaimsMapperFactory.createFounderAccessClaimsMapper();
    private final JwtClaimsMapper<FounderRefreshTokenClaims> founderRefreshClaimsMapper = JwtClaimsMapperFactory.createFounderRefreshClaimsMapper();

    public JwtGenerator(JwtSecretKeyProvider secretKeyProvider) {
        this.secretKeyProvider = secretKeyProvider;
    }

    public String generateFounderAccessToken(FounderAccessTokenClaims tokenClaims) {
        return generateAccessToken(founderAccessClaimsMapper.mapTokenClaims(tokenClaims));
    }

    public String generateFounderRefreshToken(FounderRefreshTokenClaims tokenClaims) {
        return generateRefreshToken(founderRefreshClaimsMapper.mapTokenClaims(tokenClaims));
    }

    private String generateAccessToken(Claims claims) {
        return generateToken(claims, JwtValidityTimes.ACCESS_TOKEN_VALIDITY_SECONDS);
    }

    private String generateRefreshToken(Claims claims) {
        claims.put(JwtClaimFields.REFRESH_TOKEN_ID, UUID.randomUUID().toString());
        return generateToken(claims, JwtValidityTimes.REFRESH_TOKEN_VALIDITY_SECONDS);
    }

    private String generateToken(Claims claims, int validitySeconds) {
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
