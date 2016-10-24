package com.f5live.hitmecolors.common.util;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public class CalendarUtil {
    private static final String TAG = Calendar.class.getSimpleName();

    /**
     * Init instance with default the timezone
     * */
    public static java.util.Calendar getInstance(){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        return calendar;
    }

    /**
     * Init instance and set the timezone
     * */
    public static java.util.Calendar getInstance(String timezone){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timezone));
        return calendar;
    }
    /**
     * Init instance with the timezone = 0 (UTC)
     * */
    public static java.util.Calendar getInstanceUTC(){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        return calendar;
    }

    /**
     * Init instance with the timezone local device
     * */
    public static java.util.Calendar getInstanceTimeZoneDevice(){
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        return calendar;
    }
}
