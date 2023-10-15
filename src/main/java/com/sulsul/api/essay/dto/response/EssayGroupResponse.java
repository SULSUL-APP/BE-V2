package com.sulsul.api.essay.dto.response;

import com.sulsul.api.essay.Essay;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EssayGroupResponse {

    @Schema(description = "첨삭 리스트")
    private final List<EssayResponse> essays = new ArrayList<>();

    public EssayGroupResponse(List<Essay> essays) {
        essays.stream()
                .map(EssayResponse::new)
                .forEach(essay -> this.essays.add(essay));
    }
}