package com.sulsul.api.teacherprofile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class TeacherProfileRequest {

    @Schema(description = "첨삭 가격", example = "50000")
    public final String price;
    @Schema(description = "강사 약력 및 학력", example = "강사 프로필 약력 및 학력 예시")
    @Size(min = 2, max = 1000, message = "강사 약력 및 학력은 2글자 이상 1000글자 이하입니다.")
    private final String careerDetail;
    @Schema(description = "강사 소개 문구", example = "강사 프로필 소개 문구 예시")
    @Size(min = 2, max = 1000, message = "강사 소개 문구는 2글자 이상 1000글자 이하입니다.")
    private final String introDetail;
    @Schema(description = "가능 대학", example = "첨삭 가능 대학 예시")
    @Size(min = 2, max = 1000, message = "가능 대학 소개는 2글자 이상 1000글자 이하입니다.")
    private final String possibleUniv;

    @Schema(description = "기타 사항", example = "기타 사항 문구 예시")
    @Size(min = 2, max = 1000, message = "기타 사항은 2글자 이상 1000글자 이하입니다.")
    private final String otherDetail;
}
