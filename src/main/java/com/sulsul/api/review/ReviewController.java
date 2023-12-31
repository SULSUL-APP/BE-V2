package com.sulsul.api.review;

import com.sulsul.api.common.CurrentUser;
import com.sulsul.api.exception.profile.TeacherProfileNotFoundException;
import com.sulsul.api.exception.review.InvalidReviewCreateException;
import com.sulsul.api.handler.ErrorResponse;
import com.sulsul.api.review.dto.request.ReviewRequest;
import com.sulsul.api.review.dto.response.ReviewGroupResponse;
import com.sulsul.api.review.dto.response.ReviewResponse;
import com.sulsul.api.teacherprofile.TeacherProfile;
import com.sulsul.api.teacherprofile.TeacherProfileRepository;
import com.sulsul.api.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Review", description = "리뷰 관련 API")
@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final TeacherProfileRepository teacherProfileRepository;

    @Operation(summary = "해당 첨삭에 리뷰 작성", description = "essayId에 해당하는 첨삭에 리뷰를 작성한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CREATED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/essay/{essayId}/reviews")
    public ResponseEntity<?> createReview(@Parameter(description = "리뷰를 작성할 첨삭의 id")
                                          @PathVariable Long essayId,
                                          @Valid @RequestBody ReviewRequest reviewRequest,
                                          @CurrentUser User user,
                                          BindingResult bindingResult) {
        // 리뷰 내용 유효성 검사
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            throw new InvalidReviewCreateException(errorMap);
        }

        Review review = reviewService.createReview(essayId, user, reviewRequest);
        return new ResponseEntity<>(new ReviewResponse(review), HttpStatus.CREATED);
    }

    @Operation(summary = "강사프로필에 작성된 모든 리뷰 조회", description = "profileId에 해당하는 강사프로필에 작성된 모든 리뷰를 조회한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewGroupResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/profiles/{profileId}/reviews")
    public ResponseEntity<?> getReviews(@Parameter(description = "리뷰를 조회할 강사프로필의 id값")
                                        @PathVariable Long profileId) {

        TeacherProfile profile = teacherProfileRepository.findById(profileId)
                .orElseThrow(() -> new TeacherProfileNotFoundException());

        long teacherId = profile.getTeacher().getId();
        List<Review> reviews = reviewService.getReviews(teacherId);
        return new ResponseEntity<>(new ReviewGroupResponse(reviews), HttpStatus.OK);
    }
}