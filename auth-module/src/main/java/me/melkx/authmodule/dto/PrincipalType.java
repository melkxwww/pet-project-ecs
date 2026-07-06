package me.melkx.authmodule.dto;

public enum PrincipalType {
    FOUNDER,
    EMPLOYEE;

    public static PrincipalType fromJwtTokenType(JwtTokenType jwtTokenType) {
        if(jwtTokenType == JwtTokenType.FOUNDER_ACCESS)
            return PrincipalType.FOUNDER;
        else if(jwtTokenType == JwtTokenType.EMPLOYEE_ACCESS)
            return PrincipalType.EMPLOYEE;

        throw new IllegalArgumentException("Invalid token type");
    }
}
