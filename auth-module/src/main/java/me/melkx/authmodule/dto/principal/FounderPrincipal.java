package me.melkx.authmodule.dto.principal;

import me.melkx.authmodule.dto.PrincipalType;

import java.util.UUID;

public record FounderPrincipal(UUID id) implements Principal {
    @Override
    public String getSubject() {
        return id.toString();
    }

    @Override
    public PrincipalType getType() {
        return PrincipalType.FOUNDER;
    }
}
