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
@Table(name = "bookmark")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Bookmark {

    @EmbeddedId
    private BookmarkId bookmarkId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "bookmark_time")
    @JsonProperty("bookmark_time")
    private Date bookmarkTime = new Date();

}
