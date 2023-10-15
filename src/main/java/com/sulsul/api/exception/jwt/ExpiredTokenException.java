package com.sulsul.api.exception.jwt;

import com.sulsul.api.exception.AuthenticationException;

public class ExpiredTokenException extends AuthenticationException {
    public ExpiredTokenException() {
        super("JWT_03", "AccessToken의 유효기간이 지났습니다. 토큰을 재발급 받아주세요.");
    }
}