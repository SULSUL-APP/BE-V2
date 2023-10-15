package com.sulsul.api.exception.jwt;

import com.sulsul.api.exception.AuthenticationException;

public class TokenNotFoundException extends AuthenticationException {
    public TokenNotFoundException() {
        super("JWT_04", "AccessToken을 찾을 수 없습니다.");
    }
}