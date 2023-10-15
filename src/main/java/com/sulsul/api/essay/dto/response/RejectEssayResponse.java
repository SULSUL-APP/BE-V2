package com.sulsul.api.essay.dto.response;

import com.sulsul.api.essay.Essay;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class RejectEssayResponse extends ChangeEssayStateResponse {

    @Schema(description = "거절 사유", example = "일정상 첨삭이 불가능할 것 같습니다.")
    private final String rejectDetail;

    public RejectEssayResponse(Essay essay) {
        super("첨삭요청이 거절되었습니다.", essay);
        this.rejectDetail = essay.getRejectDetail();
    }
}