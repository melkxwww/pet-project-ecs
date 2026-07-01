package me.melkx.authenticationmodule.dto;

import java.util.function.Supplier;

public record CommonPrincipal(PrincipalType type, Object principal) {
}
