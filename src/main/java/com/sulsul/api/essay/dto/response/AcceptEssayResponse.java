package com.sulsul.api.essay.dto.response;

import com.sulsul.api.essay.Essay;
import lombok.Getter;

@Getter
public class AcceptEssayResponse extends ChangeEssayStateResponse {
    public AcceptEssayResponse(Essay essay) {
        super("첨삭요청이 수락되었습니다.", essay);
    }
}