package com.example.itnews.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FollowAccountId implements Serializable {

    @Column(name = "id_follower")
    private Integer idFollower;

    @Column(name = "id_following")
    private Integer idFollowing;

}