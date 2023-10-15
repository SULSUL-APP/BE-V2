package com.sulsul.api.exception.fcm;

import com.sulsul.api.exception.FcmMessageException;

public class FcmInitException extends FcmMessageException {
    public FcmInitException() {
        super("FCM_01", "FCM Initialize 과정 중 오류발생");
    }
}