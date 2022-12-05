package com.example.itnews.dto.sqlmapping;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;

public interface ITagDTO {

    @JsonProperty("id_tag")
    Integer getIdTag();

    @JsonProperty("name")
    String getName();

    @JsonProperty("logo")
    String getLogo();

    @JsonProperty("total_post")
    Integer getTotalPost();

    @JsonProperty("total_follower")
    Integer getTotalFollower();

    @JsonProperty("status")
    Boolean getStatus();
}
