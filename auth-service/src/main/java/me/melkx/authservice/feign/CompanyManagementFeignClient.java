package me.melkx.authservice.feign;

import me.melkx.authservice.feign.dto.FounderCreationRequest;
import me.melkx.authservice.feign.dto.FounderCreationResponse;
import me.melkx.shared.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(value = "company-management-service")
public interface CompanyManagementFeignClient {
    @PostMapping("/v1/internal/founders")
    CommonResult<FounderCreationResponse> createFounder(@RequestBody FounderCreationRequest request);

    @GetMapping("/v1/internal/founders")
    CommonResult<UUID> getFounderIdByEmail(@RequestParam String email);
}
