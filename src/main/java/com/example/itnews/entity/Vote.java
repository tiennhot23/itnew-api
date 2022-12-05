package com.example.itnews.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "vote")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Vote {

    @EmbeddedId
    private VoteId voteId;

    @Column(name = "type")
    @JsonProperty("type")
    private Integer type = 0;

}
