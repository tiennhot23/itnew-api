package com.example.itnews.dto;

import com.example.itnews.dto.sqlmapping.IAccountDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountDTO {

    @JsonProperty("id_account")
    private Integer idAccount;

    @JsonProperty("account_name")
    private String accountName;

    @JsonProperty("real_name")
    private String realName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("birth")
    private String birth;

    @JsonProperty("gender")
    private Integer gender;

    @JsonProperty("company")
    private String company;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("create_date")
    private String createDate;

    @JsonProperty("status")
    private Integer status;
}
