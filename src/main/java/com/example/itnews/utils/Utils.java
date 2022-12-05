package com.example.itnews.utils;

import com.example.itnews.payloads.response.MException;
import com.example.itnews.security.exceptions.MRuntimeException;

import java.util.Calendar;
import java.util.Date;

public final class Utils {
    public static Date addHoursToJavaUtilDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Date addMinutesToJavaUtilDate(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE,minutes);
        return calendar.getTime();
    }
}
