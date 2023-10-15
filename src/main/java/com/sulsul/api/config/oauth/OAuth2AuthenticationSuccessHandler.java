package com.sulsul.api.config.oauth;

import com.sulsul.api.config.jwt.JwtTokenProvider;
import com.sulsul.api.config.jwt.dto.JwtTokenDto;
import com.sulsul.api.config.security.CustomUserDetails;
import com.sulsul.api.exception.user.UserNotFoundException;
import com.sulsul.api.refreshtoken.RefreshToken;
import com.sulsul.api.refreshtoken.RefreshTokenRepository;
import com.sulsul.api.user.UserRepository;
import com.sulsul.api.user.entity.Role;
import com.sulsul.api.user.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2ResponseJsonServlet OAuth2ResponseJsonServlet;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        CustomUserDetails oAuth2User = (CustomUserDetails) authentication.getPrincipal();
        loginSuccess(response, oAuth2User);
        log.info("response Bearer_AccessToken: {}", response.getHeader("AccessToken"));
        log.info("response Bearer_RefreshToken: {}", response.getHeader("RefreshToken"));
    }

    private void loginSuccess(HttpServletResponse response, CustomUserDetails oAuth2User) throws IOException, ServletException {

        // refresh token이 존재하는 경우 delete
        // 동작안되는 상태
        User user = oAuth2User.getUser();
        refreshTokenRepository.findByUserId(user.getId())
                .ifPresent(refreshTokenRepository::delete);

        JwtTokenDto jwtTokenDto = tokenProvider.createJwtToken(oAuth2User.getUsername());
        String userRole = isGuest(oAuth2User.getUsername());

        // refresh token 엔티티 저장
        String refreshToken = jwtTokenDto.getRefreshToken();
        RefreshToken refreshTokenEntity = new RefreshToken(user.getId(), refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);

        tokenProvider.sendAccessAndRefreshToken(response, jwtTokenDto.getAccessToken(), refreshToken);
        OAuth2ResponseJsonServlet.service(response, oAuth2User, userRole);
    }

    private String isGuest(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                UserNotFoundException::new
        );

        if (user.getUserRole().equals(Role.GUEST)) {
            return "true";
        }
        return "false";
    }
}