package me.melkx.jwtmodule.core.service;

import java.time.Duration;

public record JwtConfiguration(Duration accessTokenValidity, Duration refreshTokenValidity) {
}
