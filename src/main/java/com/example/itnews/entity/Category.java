package com.example.itnews.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Category")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    @JsonProperty("category_id")
    private Integer categoryId;
    @Column(name = "category_title")
    @JsonProperty("category_title")
    private String categoryTitle;
    @Column(name = "category_icon")
    @JsonProperty("category_icon")
    private String categoryIcon;


}
