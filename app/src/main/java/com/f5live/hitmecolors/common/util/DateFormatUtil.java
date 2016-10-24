package com.f5live.hitmecolors.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Copyright © 2016 Neo-Lab Co.,Ltd.
 * Created by VuLT on 24/10/2016.
 */

public class DateFormatUtil {
    /**
     * Given a format string and a time in milliseconds since Jan 1, 1970 GMT, returns a
     * CharSequence containing the requested date.
     * @param inFormat the format string, as described in {@link android.text.format.DateFormat}
     * @param inTimeInMillis in milliseconds since Jan 1, 1970 GMT
     * @return a {@link CharSequence} containing the requested text
     */
    public static CharSequence format(CharSequence inFormat, long inTimeInMillis) {
        return format(inFormat, new Date(inTimeInMillis));
    }

    public static CharSequence format(String timeZone, CharSequence inFormat, long inTimeInMillis) {
        return format(timeZone, inFormat, new Date(inTimeInMillis));
    }

    /**
     * Given a format string and a {@link java.util.Date} object, returns a CharSequence containing
     * the requested date.
     * @param inFormat the format string, as described in {@link android.text.format.DateFormat}
     * @param inDate the date to format
     * @return a {@link CharSequence} containing the requested text
     */
    public static CharSequence format(CharSequence inFormat, Date inDate) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getDefault());
        c.setTime(inDate);
        return format(inFormat, c);
    }

    public static CharSequence format(String timeZone, CharSequence inFormat, Date inDate) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone(timeZone));
        c.setTime(inDate);
        return format(inFormat, c);
    }


    /**
     * Given a format string and a {@link java.util.Calendar} object, returns a CharSequence
     * containing the requested date.
     * @param inFormat the format string, as described in {@link android.text.format.DateFormat}
     * @param inDate the date to format
     * @return a {@link CharSequence} containing the requested text
     */

    public static CharSequence format(CharSequence inFormat, Calendar inDate) {
        return format(inFormat, inDate, true);
    }

    /**
     * Given a format string and a {@link java.util.Calendar} object, returns a CharSequence
     * containing the requested date.
     * @param inFormat the format string, as described in {@link android.text.format.DateFormat}
     * @param inDate the date to format
     * @param timezone set the date follow your timezone
     * @return a {@link CharSequence} containing the requested text
     */
    public static CharSequence format(CharSequence inFormat, Calendar inDate, boolean timezone) {
        if(timezone) return android.text.format.DateFormat.format(inFormat, inDate);
        SimpleDateFormat fm = new SimpleDateFormat(inFormat.toString());
        fm.setTimeZone(TimeZone.getTimeZone("UTC"));
        return fm.format(inDate.getTime());
    }
}
