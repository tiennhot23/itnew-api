package com.example.itnews.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notification")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    @JsonProperty("id_notification")
    private Integer idNotification;

    @ManyToOne
    @JoinColumn(name = "id_account")
    @JsonProperty("account")
    private Account account;

    @Column(name = "content")
    @JsonProperty("content")
    private String content;

    @Column(name = "link")
    @JsonProperty("link")
    private String link;

    @Builder.Default
    @Column(name = "status")
    @JsonProperty("status")
    private Integer status = 0;

    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "notification_time")
    @JsonProperty("notification_time")
    private Date notificationTime = new Date();

    @Override
    public String toString() {
        return "Notification{" +
                "idNotification=" + idNotification +
                ", account=" + account.toString() +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                ", status=" + status +
                ", notificationTime=" + notificationTime +
                '}';
    }
}
