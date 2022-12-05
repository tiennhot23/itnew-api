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
public class BookmarkId implements Serializable {

    @Column(name = "id_account")
    private Integer idAccount;

    @Column(name = "id_post")
    private Integer idPost;

}
