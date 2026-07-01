package me.melkx.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import me.melkx.authservice.constant.AuthRequestValidationPatterns;

public record SignUpCredentialsRequest(
        @NotBlank @Pattern(regexp = AuthRequestValidationPatterns.EMAIL_REGEX_PATTERN) String email,
        @NotBlank @Pattern(regexp = AuthRequestValidationPatterns.PASSWORD_REGEX_PATTERN) String password) {
}
