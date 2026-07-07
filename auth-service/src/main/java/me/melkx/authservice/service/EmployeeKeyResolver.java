package me.melkx.authservice.service;

import me.melkx.authmodule.dto.PrincipalType;
import org.springframework.stereotype.Component;

@Component
public class EmployeeKeyResolver implements RedisKeyResolver {
    @Override
    public PrincipalType getType() {
        return PrincipalType.EMPLOYEE;
    }

    @Override
    public String resolveTokenKeyName(String userId) {
        return "employees:".concat(userId).concat(":tokens");
    }
}
