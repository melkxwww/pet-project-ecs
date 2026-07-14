package me.melkx.authservice.dto;

public record JwtTokenPairResponse(String accessToken, String refreshToken) {
}
