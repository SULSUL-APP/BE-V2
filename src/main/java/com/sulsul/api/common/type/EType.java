package com.sulsul.api.common.type;

import com.sulsul.api.exception.user.ETypeNotFoundException;

import java.util.Arrays;

public enum EType {
    NATURE("NATURE"), SOCIETY("SOCIETY");

    private final String value;

    EType(String eType) {
        this.value = eType;
    }

    public static EType getEType(String eType) {
        return Arrays.stream(EType.values())
                .filter(etype -> etype.getValue().equals(eType))
                .findAny()
                .orElseThrow(ETypeNotFoundException::new);
    }

    public String getValue() {
        return value;
    }

}

