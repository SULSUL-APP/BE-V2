package com.sulsul.api.essay.dto.request;

import com.sulsul.api.common.type.EssayState;
import com.sulsul.api.common.type.ReviewState;
import com.sulsul.api.essay.Essay;
import com.sulsul.api.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class CreateEssayRequest {
    @Schema(description = "대학명", example = "홍익대")
    @NotBlank
    @Size(min = 2, max = 20, message = "대학이름은 2글자 이상 20글자 이하입니다.")
    private String univ;

    @Schema(description = "시험년도", example = "2022년")
    @NotBlank
    @Size(min = 2, max = 8, message = "시험년도는 2글자 이상 8글자 이하입니다.")
    private String examYear;

    @Schema(description = "논술분야", example = "수리")
    @NotBlank
    @Size(min = 2, max = 8, message = "논술분야는 2글자 이상 8글자 이하입니다.")
    private String essayType;

    @Schema(description = "문의사항", example = "구체적인 첨삭 부탁드립니다.")
    @NotBlank
    @Size(min = 2, max = 200, message = "문의사항은 2글자 이상 200글자 이하입니다.")
    private String inquiry;

    @Schema(description = "첨삭파일")
    private MultipartFile essayFile;

    public Essay toEntity(User student, User teacher) {
        return Essay.builder()
                .univ(univ)
                .examYear(examYear)
                .essayType(essayType)
                .inquiry(inquiry)
                .essayState(EssayState.REQUEST)
                .reviewState(ReviewState.OFF)
                .student(student)
                .teacher(teacher)
                .build();
    }
}