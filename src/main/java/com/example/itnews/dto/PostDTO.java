package com.example.itnews.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDTO {
    @JsonProperty("id_post")
    private Integer idPost;

    @JsonProperty("id_account")
    private Integer idAccount;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("created")
    private String created;

    @JsonProperty("last_modified")
    private String lastModified;

    @JsonProperty("view")
    private Integer view;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("access")
    private Integer access;

    @JsonProperty("time_created")
    private String timeCreated;

    @JsonProperty("day_created")
    private String dayCreated;

    @JsonProperty("time_last_modified")
    private String timeLastModified;

    @JsonProperty("day_last_modified")
    private String dayLastModified;

    @JsonProperty("bookmark_status")
    private Boolean bookmarkStatus;

    @JsonProperty("total_comment")
    private Integer totalComment;

    @JsonProperty("vote_type")
    private Integer voteType;

    @JsonProperty("total_vote_down")
    private Integer totalVoteDown;

    @JsonProperty("total_vote_up")
    private Integer totalVoteUp;

    @JsonProperty("total_bookmark")
    private Integer totalBookmark;

    @JsonProperty("tags")
    private List<Integer> tags;
}
