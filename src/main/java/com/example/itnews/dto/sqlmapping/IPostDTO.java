package com.example.itnews.dto.sqlmapping;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public interface IPostDTO {

    @JsonProperty("id_post")
    Integer getIdPost();

    @JsonProperty("id_account")
    Integer getIdAccount();

    @JsonProperty("title")
    String getTitle();

    @JsonProperty("content")
    String getContent();

    @JsonProperty("created")
    String getCreated();

    @JsonProperty("last_modified")
    String getLastModified();

    @JsonProperty("view")
    Integer getView();

    @JsonProperty("status")
    Integer getStatus();

    @JsonProperty("access")
    Integer getAccess();

    @JsonProperty("time_created")
    String getTimeCreated();

    @JsonProperty("day_created")
    String getDayCreated();

    @JsonProperty("time_last_modified")
    String getTimeLastModified();

    @JsonProperty("day_last_modified")
    String getDayLastModified();

    @JsonProperty("bookmark_status")
    Boolean getBookmarkStatus();

    @JsonProperty("total_comment")
    Integer getTotalComment();

    @JsonProperty("vote_type")
    Integer getVoteType();

    @JsonProperty("total_vote_down")
    Integer getTotalVoteDown();

    @JsonProperty("total_vote_up")
    Integer getTotalVoteUp();

    @JsonProperty("total_bookmark")
    Integer getTotalBookmark();

    @JsonProperty("tags")
    List<Integer> getTags();

}
