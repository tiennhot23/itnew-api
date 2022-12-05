package com.example.itnews.dto.sqlmapping;

import com.example.itnews.entity.Account;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public interface IFeedbackDTO {

    @JsonProperty("id_feedback")
    Integer getIdFeedback();

    @JsonProperty("id_account")
    Integer getIdAccount();

    @JsonProperty("email")
    String getEmail();

    @JsonProperty("real_name")
    String getRealName();

    @JsonProperty("account_name")
    String getAccountName();

    @JsonProperty("subject")
    String getSubject();

    @JsonProperty("content")
    String getContent();

    @JsonProperty("date_time")
    String getDateTime();

    @JsonProperty("status")
    Integer getStatus();

    @JsonProperty("time")
    String getTime();

    @JsonProperty("day")
    String getDay();
}
