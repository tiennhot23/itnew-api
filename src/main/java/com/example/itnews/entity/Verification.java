package com.example.itnews.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "verification")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_verification")
    @JsonProperty("id_verification")
    private Integer idVerification;

    @ManyToOne
    @JoinColumn(name = "id_account")
    @JsonProperty("account")
    private Account account;

    @Column(name = "code")
    @JsonProperty("code")
    private String code;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    @JsonProperty("create_time")
    private Date createTime = new Date();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    @JsonProperty("end_time")
    private Date endTime = new Date();

    @Override
    public String toString() {
        return "Verification{" +
                "idVerification=" + idVerification +
                ", account=" + account.toString() +
                ", code='" + code + '\'' +
                ", createTime=" + createTime +
                ", endTime=" + endTime +
                '}';
    }
}
