package com.haosen.tools.base.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * 时间处理工具
 * PHP端返回的时间戳可能需要在末尾*1000,
 * 例：1294890859->1294890859000
 */
public class TimeUtil {

    public static String timeFormat(long timeMillis, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }

    public static String timeFormat(long timeMillis) {
        return timeFormat(timeMillis, "yyyy-MM-dd HH:mm:ss");
    }

    public static String timeFormat(String time, String pattern) {
        return timeFormat(timeFormatData(time, "yyyy-MM-dd"), pattern);
    }

    public static String currentData(String pattern) {
        return timeFormat(System.currentTimeMillis(), pattern);
    }

    public static long timeFormatData(String time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        try {
            return Objects.requireNonNull(format.parse(time)).getTime();
        } catch (Exception e) {
            Log.e(TimeUtil.class.getName(), Objects.requireNonNull(e.getMessage()));
        }
        return 0;
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH);
    }

    public static int getDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }
}
