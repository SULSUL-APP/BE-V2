package com.sulsul.api.user.dto.response;

import com.sulsul.api.user.entity.User;
import lombok.Getter;

@Getter
public class StudentResponse extends LoginResponse {
    public StudentResponse(User user) {
        super(user);
    }
}