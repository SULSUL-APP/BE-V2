package com.sulsul.api.config.security;

import com.sulsul.api.config.jwt.JwtTokenProvider;
import com.sulsul.api.exception.refresh.RefreshTokenNotFoundException;
import com.sulsul.api.refreshtoken.RefreshToken;
import com.sulsul.api.refreshtoken.RefreshTokenRepository;
import com.sulsul.api.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService implements LogoutHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {

        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        User user = jwtTokenProvider.getUserFromAccessToken(accessToken);
        RefreshToken storedToken = refreshTokenRepository.findByUserId(user.getId())
                .orElseThrow(RefreshTokenNotFoundException::new);

        log.info("[logoutService] RefreshToken 조회: {}", storedToken.getRefreshToken());

        // refresh token 삭제
        if (storedToken != null) {
            log.info("[logoutService] RefreshToken 삭제");
            refreshTokenRepository.delete(storedToken);
            log.info("[logoutService] 로그아웃 완료");
        }
    }
}