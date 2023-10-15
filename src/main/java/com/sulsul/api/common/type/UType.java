package com.sulsul.api.common.type;

import com.sulsul.api.exception.user.UTypeNotFoundException;

import java.util.Arrays;

public enum UType {
    TEACHER("TEACHER"),
    STUDENT("STUDENT");

    private final String value;

    UType(String uType) {
        this.value = uType;
    }

    public static UType getUType(String uType) {
        return Arrays.stream(UType.values())
                .filter(utype -> utype.getValue().equals(uType))
                .findAny()
                .orElseThrow(UTypeNotFoundException::new);
    }

    public String getValue() {
        return value;
    }
}
