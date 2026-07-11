package me.melkx.authmodule.strategy.jwt.dto.exp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.melkx.authmodule.dto.PrincipalType;
import me.melkx.authmodule.strategy.jwt.dto.TypedTokenPayload;
import me.melkx.jwtmodule.core.dto.TokenType;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class EmployeeAccessTokenPayload implements TypedTokenPayload {
    private final UUID sub;
    private final UUID companyId;

    private final PrincipalType principalType;
    private final TokenType tokenType;

    @Override
    public PrincipalType getPrincipalType() {
        return ;
    }

    @Override
    public TokenType getTokenType() {
        return null;
    }
}
