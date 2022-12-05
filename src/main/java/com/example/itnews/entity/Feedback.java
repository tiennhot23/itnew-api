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
@Table(name = "feedback")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feedback")
    @JsonProperty("id_feedback")
    private Integer idFeedback;

    @ManyToOne
    @JoinColumn(name = "id_account")
    @JsonProperty("account")
    private Account account;

    @Column(name = "subject")
    @JsonProperty("subject")
    private String subject;

    @Column(name = "content")
    @JsonProperty("content")
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_time")
    @JsonProperty("date_time")
    private Date dateTime = new Date();

    @Column(name = "status")
    @JsonProperty("status")
    private Integer status = 0;


}
