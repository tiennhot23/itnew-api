package com.example.itnews.security.exceptions;

import com.example.itnews.payloads.response.MResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException exception
    ) throws IOException, ServletException {
        logger.error("AuthenticationException: {}", "Đăng nhập thất bại");
        logResponse(HttpStatus.UNAUTHORIZED, response, exception);
    }

    private void logResponse(HttpStatus httpStatus, HttpServletResponse response, RuntimeException exception) throws IOException {
        MResponse<Object> mResponse = new MResponse<>("Thất bại: " + exception.getMessage());
        response.setStatus(httpStatus.value());
        OutputStream outputStream = response.getOutputStream();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, mResponse);
        outputStream.flush();
    }
}
