package com.sulsul.api.config.jwt;

import com.sulsul.api.exception.AccessNotAllowedException;
import com.sulsul.api.exception.BaseException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class JwtExceptionHandler {

    public static void handle(HttpServletResponse response, AccessNotAllowedException exception) throws BaseException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.getWriter().print(exception.getMessage());
    }
}