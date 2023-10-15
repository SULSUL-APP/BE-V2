package com.sulsul.api.config.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sulsul.api.config.oauth.dto.OAuth2ResponseJsonDto;
import com.sulsul.api.config.security.CustomUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@WebServlet(name = "responseJsonServlet")
public class OAuth2ResponseJsonServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void service(HttpServletResponse response, CustomUserDetails oAuth2User, String userRole) throws ServletException, IOException {

        //Content-Type: application/json
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        OAuth2ResponseJsonDto oAuth2ResponseJsonDto = OAuth2ResponseJsonDto.builder()
                .name(oAuth2User.getName())
                .email(oAuth2User.getUsername())
                .isGuest(userRole).build();

        //class를 파싱하여 json 형식 string으로 변환
        String result = objectMapper.writeValueAsString(oAuth2ResponseJsonDto);
        response.getWriter().write(result);
    }
}