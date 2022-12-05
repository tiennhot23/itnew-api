package com.example.itnews.security.exceptions;

import com.example.itnews.payloads.response.MResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j(topic = "CustomAccessDeniedHandler")
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exception
    ) throws IOException, ServletException {
        log.error("Access denied: {}", exception.getLocalizedMessage());
        HttpStatus httpStatus = HttpStatus.FORBIDDEN; // 403
        MResponse<Object> mResponse = new MResponse<>(exception.getMessage());
        response.setStatus(httpStatus.value());
        OutputStream outputStream = response.getOutputStream();
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(outputStream, mResponse);
        outputStream.flush();
    }
}
