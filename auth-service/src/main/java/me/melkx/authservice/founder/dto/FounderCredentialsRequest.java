package me.melkx.authservice.founder.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import me.melkx.authmodule.constant.AuthValidationPatterns;

public record FounderCredentialsRequest(
        @NotNull @Pattern(regexp = AuthValidationPatterns.EMAIL_REGEX_PATTERN) String email,
        @NotNull @Pattern(regexp = AuthValidationPatterns.PASSWORD_REGEX_PATTERN) String password) {
}
