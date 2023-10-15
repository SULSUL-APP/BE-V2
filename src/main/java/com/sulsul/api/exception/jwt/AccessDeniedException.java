package com.sulsul.api.exception.jwt;

import com.sulsul.api.exception.AccessNotAllowedException;

public class AccessDeniedException extends AccessNotAllowedException {
    public AccessDeniedException() {
        super("JWT_02", "접근이 금지되었습니다.");
    }
}