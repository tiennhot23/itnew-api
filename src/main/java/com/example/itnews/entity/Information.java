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
@Table(name = "information")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Information {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_information")
    @JsonProperty("id_information")
    private Integer idInformation;

    @Column(name = "logo")
    @JsonProperty("logo")
    private String logo;

    @Column(name = "name")
    @JsonProperty("name")
    private String name;

    @Column(name = "facebook")
    @JsonProperty("facebook")
    private String facebook;

    @Column(name = "android")
    @JsonProperty("android")
    private String android;

    @Column(name = "ios")
    @JsonProperty("ios")
    private String ios;

    @Column(name = "avatar")
    @JsonProperty("avatar")
    private String avatar;

    @Column(name = "logo_tag")
    @JsonProperty("logo_tag")
    private String logoTag;

    @Column(name = "token_valid")
    @JsonProperty("token_valid")
    private Integer tokenValid;

    @Column(name = "code_confirm")
    @JsonProperty("code_confirm")
    private Integer codeConfirm;

}
