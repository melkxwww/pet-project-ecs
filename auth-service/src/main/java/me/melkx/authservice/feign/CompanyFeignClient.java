package me.melkx.authservice.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "company-service")
public class CompanyFeignClient {
}
