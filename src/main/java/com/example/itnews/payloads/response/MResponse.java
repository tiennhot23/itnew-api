package com.example.itnews.payloads.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MResponse<T> {
    private String message;
    private String accessToken;
    private T data;

    public MResponse(String message, @Nullable String accessToken, T data) {
        this.message = message;
        this.accessToken = accessToken;
        this.data = data;
    }

    public MResponse(String message) {
        this.message = message;
    }

    public MResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public MResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
