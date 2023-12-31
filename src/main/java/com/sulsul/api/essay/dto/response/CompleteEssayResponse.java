package com.sulsul.api.essay.dto.response;

import com.sulsul.api.essay.Essay;
import lombok.Getter;

@Getter
public class CompleteEssayResponse extends ChangeEssayStateResponse {
    public CompleteEssayResponse(Essay essay) {
        super("첨삭이 완료되었습니다.", essay);
    }
}