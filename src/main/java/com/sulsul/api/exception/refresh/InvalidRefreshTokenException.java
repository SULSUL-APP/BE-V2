package com.sulsul.api.exception.refresh;

import com.sulsul.api.exception.AuthorizationException;

public class InvalidRefreshTokenException extends AuthorizationException {
    public InvalidRefreshTokenException() {
        super("REFRESH_02", "RefreshToken이 유효하지 않습니다.");
    }
}