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
@Table(name = "comment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cmt")
    @JsonProperty("id_cmt")
    private Integer idCmt;

    @ManyToOne
    @JoinColumn(name = "id_account")
    @JsonProperty("account")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "id_post")
    @JsonProperty("post")
    private Post post;

    @Column(name = "content")
    @JsonProperty("content")
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time")
    @JsonProperty("date_time")
    private Date dateTime = new Date();

    @Column(name = "id_cmt_parent")
    @JsonProperty("id_cmt_parent")
    private Integer idCmtParent = 0;

    @Column(name = "status")
    @JsonProperty("status")
    private Integer status = 0;

}
