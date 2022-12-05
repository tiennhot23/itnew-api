package com.example.itnews.dto.sqlmapping;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface ILockAccountDTO {

    @JsonProperty("id_account_lock")
    Integer getIdAccountLock();

    @JsonProperty("id_account_boss")
    Integer getIdAccountBoss();

    @JsonProperty("reason")
    String getReason();

    @JsonProperty("time_start_lock")
    String getTimeStartLock();

    @JsonProperty("hours_lock")
    Integer getHoursLock();

    @JsonProperty("time_end_lock")
    String getTimeEndLock();

    @JsonProperty("boss_account")
    String getBossAccount();

    @JsonProperty("locker_name")
    String getLockerName();

    @JsonProperty("locker_account")
    String getLockerAccount();

    @JsonProperty("day_start")
    String getDayStart();

    @JsonProperty("time_start")
    String getTimeStart();

    @JsonProperty("day_end")
    String getDayEnd();

    @JsonProperty("time_end")
    String getTimeEnd();

}
