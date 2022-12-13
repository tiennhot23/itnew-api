package com.example.itnews.payloads.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;


@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Username không được trống")
    @NotEmpty(message = "Username mới không được trống")
    @JsonProperty("account_name")
    private String username;

    @NotBlank(message = "Password không được trống")
    @NotEmpty(message = "Password không được trống")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
