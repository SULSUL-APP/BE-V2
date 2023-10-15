package com.sulsul.api.exception.refresh;

import com.sulsul.api.exception.AuthorizationException;

public class RefreshTokenMismatchException extends AuthorizationException {
    public RefreshTokenMismatchException() {
        super("REFRESH_04", "유저의 RefreshToken값이 일치하지 않습니다.");
    }
}