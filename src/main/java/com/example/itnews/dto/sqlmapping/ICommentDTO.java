package com.example.itnews.dto.sqlmapping;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ICommentDTO {

    @JsonProperty("id_account")
    Integer getIdAccount();

    @JsonProperty("id_role")
    Integer getIdRole();

    @JsonProperty("account_name")
    String getAccountName();

    @JsonProperty("real_name")
    String getRealName();

    @JsonProperty("avatar")
    String getAvatar();

    @JsonProperty("id_cmt")
    Integer getIdCmt();

    @JsonProperty("content")
    String getContent();

    @JsonProperty("id_cmt_parent")
    Integer getIdCmtParent();

    @JsonProperty("status")
    Integer getStatus();

    @JsonProperty("day")
    String getDay();

    @JsonProperty("time")
    String getTime();
}
