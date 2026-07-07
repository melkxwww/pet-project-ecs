package me.melkx.authmodule.dto.principal;

import me.melkx.authmodule.dto.PrincipalType;

public record CommonPrincipal(PrincipalType type, Object principal) {
}
