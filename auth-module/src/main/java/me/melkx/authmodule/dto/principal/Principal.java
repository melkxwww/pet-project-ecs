package me.melkx.authmodule.dto.principal;

import me.melkx.authmodule.dto.PrincipalType;

public sealed interface Principal
        permits EmployeePrincipal, FounderPrincipal {
    String getSubject();

    PrincipalType getType();
}
