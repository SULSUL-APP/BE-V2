package com.sulsul.api.essay.dto.request;

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
public class RejectRequest {
    @Schema(description = "거절사유", example = "거절사유 예시")
    @NotBlank
    @Size(min = 2, max = 20, message = "거절사유는 2글자 이상 20글자 이하입니다.")
    private final String rejectDetail;
}