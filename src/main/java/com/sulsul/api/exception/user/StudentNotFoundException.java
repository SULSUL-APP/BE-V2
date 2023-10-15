package com.sulsul.api.exception.user;

import com.sulsul.api.exception.ResourceNotFoundException;

import java.util.Map;

public class StudentNotFoundException extends ResourceNotFoundException {
    public StudentNotFoundException(long userId) {
        super("USER_01", "해당 학생을 찾을 수 없습니다.", Map.of("userId", String.valueOf(userId)));
    }
}