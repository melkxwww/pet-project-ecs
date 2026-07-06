package me.melkx.authmodule.dto.jwt;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import me.melkx.authmodule.dto.JwtTokenType;
import me.melkx.jwtmodule.core.service.TokenPayload;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "user_type",
        visible = true
)@JsonSubTypes({
        @JsonSubTypes.Type(value = FounderAccessTokenPayload.class, name = "founder_access"),
        @JsonSubTypes.Type(value = FounderRefreshTokenPayload.class, name = "founder_refresh"),
        @JsonSubTypes.Type(value = EmployeeAccessTokenPayload.class, name = "employee_access"),
        @JsonSubTypes.Type(value = EmployeeRefreshTokenPayload.class, name = "employee_refresh")
})
public interface TypedTokenPayload extends TokenPayload {
    JwtTokenType getTokenType();
}
