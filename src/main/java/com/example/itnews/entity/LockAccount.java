package com.example.itnews.entity;

import com.example.itnews.utils.Utils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "lock_account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LockAccount {

    @EmbeddedId
    private LockAccountId lockAccountId;

    @Column(name = "reason")
    @JsonProperty("reason")
    private String reason;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_start_lock")
    @JsonProperty("time_start_lock")
    private Date timeStartLock = new Date();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_end_lock")
    @JsonProperty("time_end_lock")
    private Date timeEndLock = new Date();

    @Column(name = "hours_lock")
    @JsonProperty("hours_lock")
    private Integer hoursLock;

    public LockAccount(LockAccountId lockAccountId, String reason, Integer hoursLock) {
        this(lockAccountId, reason, new Date(), Utils.addHoursToJavaUtilDate(new Date(), hoursLock), hoursLock);
    }

    @Override
    public String toString() {
        return "LockAccount{" +
                "lockAccountId=" + lockAccountId.toString() +
                ", reason='" + reason + '\'' +
                ", timeStartLock=" + timeStartLock +
                ", timeEndLock=" + timeEndLock +
                ", hoursLock=" + hoursLock +
                '}';
    }
}
