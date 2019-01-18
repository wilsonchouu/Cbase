package com.cbase.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author : zhouyx
 * @date : 2017/9/12
 * @description : 时间工具
 */
public class DateUtils {

    /**
     * String转Date
     *
     * @param str
     * @param style
     * @return
     */
    public static Date parseToDate(String str, String style) {
        if (str == null || style == null) {
            return new Date();
        }
        try {
            SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateTimeInstance();
            format.applyPattern(style);
            return format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    /**
     * String转long
     *
     * @param str
     * @param style
     * @return
     */
    public static long parseToLong(String str, String style) {
        if (str == null || style == null) {
            return System.currentTimeMillis();
        }
        Date date = parseToDate(str, style);
        if (date == null) {
            return System.currentTimeMillis();
        }
        return date.getTime();
    }

    /**
     * 改变日期格式
     *
     * @param str
     * @param fromStyle
     * @param toStyle
     * @return
     */
    public static String convert(String str, String fromStyle, String toStyle) {
        if (str == null || fromStyle == null || toStyle == null) {
            return "";
        }
        try {
            SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateTimeInstance();
            format.applyPattern(fromStyle);
            Date date = format.parse(str);
            format.applyPattern(toStyle);
            return format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Date转String
     *
     * @param date
     * @param style
     * @return
     */
    public static String parseToString(Date date, String style) {
        if (date == null || style == null) {
            return "";
        }
        SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateTimeInstance();
        format.applyPattern(style);
        return format.format(date);
    }

    /**
     * long转String
     *
     * @param time
     * @param style
     * @return
     */
    public static String parseToString(long time, String style) {
        if (time == 0 || style == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateTimeInstance();
        format.applyPattern(style);
        return format.format(calendar.getTime());
    }

    /**
     * 当前时间转String
     *
     * @param style
     * @return
     */
    public static String formatCurrentTime(String style) {
        if (style == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateTimeInstance();
        format.applyPattern(style);
        return format.format(calendar.getTime());
    }

    /**
     * 时间偏移
     *
     * @param time   毫秒
     * @param field  偏移单位
     *               {@link java.util.Calendar#YEAR}
     *               {@link java.util.Calendar#MONTH}
     *               {@link java.util.Calendar#DAY_OF_MONTH}
     *               ...etc.
     * @param offset 偏移数值
     * @param style  输出格式
     * @return
     */
    public static String offsetTime(long time, int field, int offset, String style) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(field, offset);
        SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateTimeInstance();
        format.applyPattern(style);
        return format.format(calendar.getTime());
    }

    /**
     * 时间向前偏移offset个field
     *
     * @param time
     * @param field
     * @param offset
     * @param style
     * @return
     */
    public static String previousTime(long time, int field, int offset, String style) {
        return offsetTime(time, field, -offset, style);
    }

    /**
     * 时间向后偏移offset个field
     *
     * @param time
     * @param field
     * @param offset
     * @param style
     * @return
     */
    public static String nextTime(long time, int field, int offset, String style) {
        return offsetTime(time, field, offset, style);
    }

    /**
     * 当前时间与目标时间比较，1小时内用分钟表示，24小时内用小时表示，其余按需求格式显示
     *
     * @param time
     * @param style
     * @return
     */
    public static String formatDifferTime(long time, String style) {
        // 这样得到的差值是微秒级别
        long differ = new Date(System.currentTimeMillis()).getTime() - new Date(time).getTime();
        long minute = differ / 60000;
        // 是否大于一天
        // 60 * 24 = 1440
        if (minute < 1440) {
            // 是否大于一小时
            if (minute >= 60) {
                return differ / 60 + "小时前";
            } else {
                return differ + "分钟前";
            }
        } else {
            return parseToString(time, style);
        }
    }

    /**
     * 秒数时间格式化为00:00:00
     *
     * @param second
     * @return
     */
    public static String formatTime(long second) {
        String hs, ms, ss, formatTime;

        long h = second / 3600;
        long m = (second % 3600) / 60;
        long s = (second % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
        formatTime = hs + ":" + ms + ":" + ss;
        return formatTime;
    }

}
