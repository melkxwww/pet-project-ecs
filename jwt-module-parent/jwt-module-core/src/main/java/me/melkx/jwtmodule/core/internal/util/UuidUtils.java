package me.melkx.jwtmodule.core.internal.util;

import java.util.Objects;
import java.util.UUID;

public class UuidUtils {
    public static UUID parseUUID(String uuid) {
        Objects.requireNonNull(uuid, "UUID string cannot be null");
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format: " + uuid);
        }
    }
}
