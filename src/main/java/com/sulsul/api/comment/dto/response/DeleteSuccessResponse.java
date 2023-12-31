package com.sulsul.api.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class DeleteSuccessResponse {

    @Schema(description = "삭제 확인 메시지")
    private final String message;

    public DeleteSuccessResponse() {
        this.message = "댓글 삭제 성공";
    }
}