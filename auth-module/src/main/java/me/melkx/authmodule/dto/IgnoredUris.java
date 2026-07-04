package me.melkx.authmodule.dto;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class IgnoredUris {
    private final List<String> uris;

    public IgnoredUris(String... uris) {
        this.uris = Arrays.asList(uris);
    }
}
