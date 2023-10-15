package com.sulsul.api.exception.notification;

import com.sulsul.api.exception.BadInputException;

import java.util.Map;

public class InvalidCommonNotiRequestException extends BadInputException {
    public InvalidCommonNotiRequestException(Map<String, String> errorMap) {
        super("NOTI_01", "전체알림 전송요청이 유효하지 않습니다.", errorMap);
    }
}