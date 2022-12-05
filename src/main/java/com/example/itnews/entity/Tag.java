package com.example.itnews.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tag")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tag")
    @JsonProperty("id_tag")
    private Integer idTag;

    @Column(name = "name")
    @JsonProperty("name")
    private String name;

    @Column(name = "logo")
    @JsonProperty("logo")
    private String logo;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "post_tag", joinColumns = @JoinColumn(name = "id_tag"), inverseJoinColumns = @JoinColumn(name = "id_post"))
//    private List<Post> posts;
}
