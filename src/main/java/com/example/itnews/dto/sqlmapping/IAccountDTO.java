package com.example.itnews.dto.sqlmapping;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;

public interface IAccountDTO {

    @JsonProperty("id_account")
    Integer getIdAccount();

    @JsonProperty("id_role")
    Integer getIdRole();


    @JsonProperty("role")
    String getRole();

    @JsonProperty("account_name")
    String getAccountName();

    @JsonProperty("real_name")
    String getRealName();

    @JsonProperty("email")
    String getEmail();

    @JsonProperty("password")
    String getPassword();

    @JsonProperty("avatar")
    String getAvatar();

    @JsonProperty("birth")
    String getBirth();

    @JsonProperty("gender")
    Integer getGender();

    @JsonProperty("company")
    String getCompany();

    @JsonProperty("phone")
    String getPhone();

    @JsonProperty("create_date")
    String getCreateDate();

    @JsonProperty("status")
    Boolean getStatus();

    @JsonProperty("account_status")
    Integer getAccountStatus();

    @JsonProperty("total_post")
    Integer getTotalPost();

    @JsonProperty("total_tag_follow")
    Integer getTotalTagFollow();

    @JsonProperty("total_follower")
    Integer getTotalFollower();

    @JsonProperty("total_following")
    Integer getTotalFollowing();

    @JsonProperty("total_view")
    Integer getTotalView();

    @JsonProperty("total_vote_up")
    Integer getTotalVoteUp();

    @JsonProperty("total_vote_down")
    Integer getTotalVoteDown();

}
