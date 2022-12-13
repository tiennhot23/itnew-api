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
public class LockAccountDTO {
    @NotNull(message = "Lí do không được trống")
    @NotEmpty(message = "Lí do không được trống")
    @JsonProperty("reason")
    private String reason;

//    @JsonProperty("time_start_lock")
//    private Date timeStartLock;
//
//    @JsonProperty("time_end_lock")
//    private Date timeEndLock;

    @NotNull(message = "Thời gian khoá không được trống")
    @JsonProperty("hours_lock")
    private Integer hoursLock;

}
