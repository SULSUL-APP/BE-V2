package com.sulsul.api.exception.refresh;

import com.sulsul.api.exception.AuthorizationException;

public class RefreshTokenNotFoundException extends AuthorizationException {
    public RefreshTokenNotFoundException() {
        super("REFRESH_03", "유저의 RefreshToken을 찾을 수 없습니다.");
    }
}