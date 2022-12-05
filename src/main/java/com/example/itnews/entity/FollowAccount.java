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
@Table(name = "follow_account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FollowAccount {

    @EmbeddedId
    private FollowAccountId followAccountId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "follow_time")
    @JsonProperty("follow_time")
    private Date followTime = new Date();

}

