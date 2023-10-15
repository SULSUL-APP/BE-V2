package com.sulsul.api.exception.auth;

import com.sulsul.api.exception.AuthenticationException;

public class NotAuthenticatedException extends AuthenticationException {
    public NotAuthenticatedException() {
        super("AUTH_01", "인증되지 않은 유저입니다.");
    }
}