package com.sulsul.api.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class CommentRequest {
    @Schema(description = "댓글 내용", example = "댓글 내용 예시")
    @NotBlank
    @Size(min = 2, max = 100, message = "댓글은 2글자 이상 100글자 이하입니다.")
    private final String detail;
}