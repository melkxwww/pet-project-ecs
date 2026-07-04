package me.melkx.authservice.feign.dto;

public record FounderCreationRequest(String email, String passwordHash) {
}
