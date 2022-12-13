package com.example.itnews.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordDTO {
    @NotNull(message = "Password mới không được trống")
    @NotEmpty(message = "Password mới không được trống")
    @JsonProperty("new_password")
    private String newPassword;

    @NotNull(message = "Password cũ không được trống")
    @NotEmpty(message = "Password cũ không được trống")
    @JsonProperty("old_password")
    private String oldPassword;

}
