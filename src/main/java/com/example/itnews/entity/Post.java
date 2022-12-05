package com.example.itnews.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_post")
    @JsonProperty("id_post")
    private Integer idPost;

    @ManyToOne
    @JoinColumn(name = "id_account")
    @JsonProperty("account")
    private Account account;

    @Column(name = "title")
    @JsonProperty("title")
    private String title;

    @Column(name = "content")
    @JsonProperty("content")
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created")
    @JsonProperty("created")
    private Date created = new Date();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modified")
    @JsonProperty("last_modified")
    private Date lastModified = new Date();

    @Column(name = "view")
    @JsonProperty("view")
    private Integer view = 0;

    @Column(name = "status")
    @JsonProperty("status")
    private Integer status = 0;

    @Column(name = "access")
    @JsonProperty("access")
    private Integer access = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "post_tag", joinColumns = @JoinColumn(name = "id_post"), inverseJoinColumns = @JoinColumn(name = "id_tag"))
    @JsonProperty("tags")
    private List<Tag> tags;

}
