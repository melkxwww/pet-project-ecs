package me.melkx.authmodule.dto;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Getter
@ConfigurationProperties(prefix = "app.auth")
public class AuthenticationIgnoredUris {
    private List<String> ignoredUris = new ArrayList<>();
}
