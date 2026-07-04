package me.melkx.authservice.service;

import me.melkx.authservice.dto.SignUpCredentialsRequest;
import me.melkx.authservice.feign.CompanyManagementFeignClient;
import me.melkx.authservice.feign.dto.FounderCreationRequest;
import me.melkx.authservice.feign.dto.FounderCreationResponse;
import me.melkx.shared.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class FounderService {
    private final CompanyManagementFeignClient companyManagementClient;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public FounderService(CompanyManagementFeignClient companyManagementClient, PasswordEncoder passwordEncoder) {
        this.companyManagementClient = companyManagementClient;
        this.passwordEncoder = passwordEncoder;
    }

    public UUID createFounder(SignUpCredentialsRequest credentials) {
        CommonResult<FounderCreationResponse> result = companyManagementClient.createFounder(new FounderCreationRequest(
                credentials.email(),
                passwordEncoder.encode(credentials.password())
        ));

        if(!result.isSuccess())
            throw CommonResult.mapToFeignException(result);

        return Objects.requireNonNull(result.getData(), "Result data cannot be null")
                .id();
    }
}
