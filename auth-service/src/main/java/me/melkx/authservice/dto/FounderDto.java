package me.melkx.authservice.dto;

import java.util.UUID;

public record FounderDto(UUID uuid, String email, String passwordHash) {
}
