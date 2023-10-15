package com.sulsul.api.fcm;

import com.sulsul.api.common.CurrentUser;
import com.sulsul.api.fcm.dto.FcmTokenRequest;
import com.sulsul.api.fcm.dto.FcmTokenSettingSuccess;
import com.sulsul.api.handler.ErrorResponse;
import com.sulsul.api.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FcmToken", description = "FcmToken 설정 API")
@RestController
@RequiredArgsConstructor
public class FcmTokenController {

    private final FcmTokenService fcmTokenService;

    @Operation(summary = "FcmToken 전달", description = "FcmToken을 전달받아 저장한다. (소셜로그인 이후 호출)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FcmTokenSettingSuccess.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "FORBIDDEN",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "NOT FOUND",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/fcmtoken")
    public ResponseEntity<?> saveFcmToken(@RequestBody FcmTokenRequest request,
                                          @CurrentUser User user) {
        String fcmToken = request.getFcmToken();
        fcmTokenService.saveFcmToken(user, fcmToken);
        return new ResponseEntity<>(new FcmTokenSettingSuccess(), HttpStatus.OK);
    }
}