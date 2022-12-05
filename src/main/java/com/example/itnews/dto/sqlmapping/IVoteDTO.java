package com.example.itnews.dto.sqlmapping;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface IVoteDTO {

    @JsonProperty("id_account")
    Integer getIdAccount();

    @JsonProperty("id_post")
    Integer getIdPost();

    @JsonProperty("type")
    Integer getType();

    @JsonProperty("total_vote_up")
    Integer getTotalVoteUp();

    @JsonProperty("total_vote_down")
    Integer getTotalVoteDown();
}
