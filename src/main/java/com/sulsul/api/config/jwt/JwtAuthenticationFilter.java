package com.sulsul.api.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 토큰으로 인증하고 SecurityContextHolder에 추가하는 필터를 가진 클래스
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        log.info("[JwtAuthenticationFilter] AccessToken 값 추출 완료: {}", accessToken);

        try {
            // AccessToken이 만료된 경우 GET /refresh로 재발급
            // GET /refresh에서 RefreshToken도 만료된 것을 확인하면 강제 로그아웃 요청
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }
}