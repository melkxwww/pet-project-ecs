package me.melkx.authservice.security.provider;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SignInPublicIdResolverCoordinator {
    private final Map<Class<?>, SignInPublicIdResolver> resolversCache = new ConcurrentHashMap<>();
    private final Set<SignInPublicIdResolver> idResolvers;

    public SignInPublicIdResolverCoordinator(Set<SignInPublicIdResolver> idResolvers) {
        this.idResolvers = idResolvers;
    }

    public UUID resolvePublicId(Object unresolvedId) {
        Class<?> idClass = unresolvedId.getClass();

        return resolversCache.computeIfAbsent(idClass, clazz ->
                idResolvers.stream()
                        .filter(resolver -> resolver.supports(clazz))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Unsupported id type: " + clazz.getName()))
        ).resolvePublicId(unresolvedId);
    }
}
