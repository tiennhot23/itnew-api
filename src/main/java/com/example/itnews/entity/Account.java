package com.example.itnews.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_account")
    @JsonProperty("id_account")
    private Integer idAccount;

    @ManyToOne
    @JoinColumn(name = "id_role")
    @JsonProperty("role")
    private Role role;

    @Column(name = "account_name")
    @JsonProperty("account_name")
    private String accountName;

    @Column(name = "real_name")
    @JsonProperty("real_name")
    private String realName;

    @Column(name = "email")
    @JsonProperty("email")
    private String email;

    @Column(name = "password")
    @JsonProperty("password")
    @Basic(fetch = FetchType.LAZY)
    private String password;

    @Column(name = "avatar")
    @JsonProperty("avatar")
    private String avatar = "https://lh3.googleusercontent.com/proxy/250lbgVm0ovEDb_HX2CFetuHny9dOyO3wghph7CIQpQ0FeLwobZpDDMbTM6sXhADOzFlsKv9-gZ6Rze5brn2zDoWWRXploBSe_2t_PgWh9AyWj8";

    @Column(name = "birth")
    @JsonProperty("birth")
    private Date birth;

    @Column(name = "gender")
    @JsonProperty("gender")
    private Integer gender = 0;

    @Column(name = "company")
    @JsonProperty("company")
    private String company;

    @Column(name = "phone")
    @JsonProperty("phone")
    private String phone;

    @Column(name = "create_date")
    @JsonProperty("create_date")
    private Date createDate = new Date();

    @Column(name = "status")
    @JsonProperty("status")
    private Integer status = 0;

//    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
//    private List<Post> posts;

    @Override
    public String toString() {
        return "Account{" +
                "idAccount=" + idAccount +
                ", role=" + role.getName() +
                ", accountName='" + accountName + '\'' +
                ", realName='" + realName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", avatar='" + avatar + '\'' +
                ", birth=" + birth.toString() +
                ", gender=" + gender +
                ", company='" + company + '\'' +
                ", phone='" + phone + '\'' +
                ", createDate=" + createDate +
                ", status=" + status +
                '}';
    }
}
