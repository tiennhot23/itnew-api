package com.example.itnews.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateAccountDTO {
    @NotNull(message = "Account name không được trống")
    @NotEmpty(message = "Account name không được trống")
    @JsonProperty("account_name")
    private String accountName;

    @NotNull(message = "Real name không được trống")
    @NotEmpty(message = "Real name không được trống")
    @JsonProperty("real_name")
    private String realName;

    @NotNull(message = "Email không được trống")
    @NotEmpty(message = "Email không được trống")
    @JsonProperty("email")
    private String email;

    @NotNull(message = "Password không được trống")
    @NotEmpty(message = "Password không được trống")
    @JsonProperty("password")
    private String password;
}
