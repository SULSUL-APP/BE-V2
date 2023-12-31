package com.sulsul.api.refreshtoken;

import com.sulsul.api.config.jwt.JwtTokenProvider;
import com.sulsul.api.config.jwt.dto.JwtTokenDto;
import com.sulsul.api.exception.refresh.RefreshTokenMismatchException;
import com.sulsul.api.exception.refresh.RefreshTokenNotFoundException;
import com.sulsul.api.exception.user.UserNotFoundException;
import com.sulsul.api.user.UserRepository;
import com.sulsul.api.user.entity.User;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * user의 RefreshToken이 맞다면 AccessToken을 재발급한다.
     * RefreshToken의 만료일이 1일 이내라면 RefreshToken을 재발급한다.
     *
     * @param refreshToken 유저의 refreshToken
     * @return TokenDto 반환
     */
    @Transactional
    public JwtTokenDto refresh(String refreshToken, Claims claims) {
        JwtTokenDto tokensDto = new JwtTokenDto();
        String email = claims.getSubject();

        // subject로 유저정보 조회, 해당유저가 없다면 예외처리
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        // 유저의 RefreshToken 조회
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUserId(user.getId())
                .orElseThrow(RefreshTokenNotFoundException::new);

        // 유저의 RefreshToken이 맞는지 확인
        if (!refreshTokenEntity.getRefreshToken().equals(refreshToken)) {
            throw new RefreshTokenMismatchException();
        }

        // AccessToken 재발급
        String accessToken = jwtTokenProvider.createAccessToken(email, new Date());
        tokensDto.setAccessToken(accessToken);

        // Refresh 토큰 만료일 계산
        long expiration = jwtTokenProvider.getRefreshTokenClaims(refreshToken)
                .getExpiration().getTime();
        long now = new Date().getTime();
        long diffTime = expiration - now;

        // 1일 이내에 만료된다면 RefreshToken 재발급
        if (diffTime < 86400000) {
            String newRefreshToken = jwtTokenProvider.createRefreshToken(new Date());
            refreshTokenEntity.updateRefreshToken(newRefreshToken);
            // 재발급된 refresh token 저장
            refreshTokenRepository.save(refreshTokenEntity);
            tokensDto.setRefreshToken(newRefreshToken);
        }

        return tokensDto;
    }
}