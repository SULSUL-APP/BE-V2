package com.sulsul.api.exception;

import java.util.Map;

public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(String code, String message) {
        super(code, message);
    }

    public ResourceNotFoundException(String code, String message, Map<String, String> errors) {
        super(code, message, errors);
    }
}