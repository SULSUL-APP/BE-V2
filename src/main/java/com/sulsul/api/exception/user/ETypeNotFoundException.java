package com.sulsul.api.exception.user;

import com.sulsul.api.exception.ResourceNotFoundException;

public class ETypeNotFoundException extends ResourceNotFoundException {

    public ETypeNotFoundException() {
        super("USER_05", "해당 논술분야 타입을 찾을 수 없습니다.");
    }

}
