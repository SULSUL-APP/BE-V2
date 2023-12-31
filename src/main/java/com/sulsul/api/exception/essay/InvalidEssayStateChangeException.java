package com.sulsul.api.exception.essay;

import com.sulsul.api.exception.ResourceNotFoundException;

import java.util.Map;

public class InvalidEssayStateChangeException extends ResourceNotFoundException {
    public InvalidEssayStateChangeException(long essayId) {
        super("ESSAY_06", "첨삭상태 변경이 유효하지 않습니다.",
                Map.of("essayId", String.valueOf(essayId)));
    }
}