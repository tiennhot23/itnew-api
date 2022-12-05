package com.example.itnews.security.exceptions;

import com.example.itnews.payloads.response.MException;
import com.example.itnews.payloads.response.MResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<MResponse<Object>> handleEntityNotFound(EntityNotFoundException ex){
        MResponse<Object> mResponse = new MResponse<>("Entity not found: " + ex.getMessage(), null, null );

        return new ResponseEntity<>(mResponse, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        MResponse<Object> mResponse = new MResponse<>("Validation Error: " + ex.getBindingResult().toString(), null, null);

        return new ResponseEntity<>(mResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(MException ex, WebRequest request) {
        MResponse<Object> mResponse = new MResponse<>(ex.getMessage(), null, null );

        return new ResponseEntity<>(mResponse, ex.getHttpStatus());
    }
}
