package com.yulin.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 格式化时间
 */
public class DateUtil {

    private static final String[] WEEK_DAYS = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    private static final SimpleDateFormat SDF_yyyyMMdd = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);
    private static final SimpleDateFormat SDF_YYYY_MM_DD = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
    private static final SimpleDateFormat SDF_HH_MM = new SimpleDateFormat("HH:mm", Locale.CHINA);
    private static final SimpleDateFormat SDF_MM_DD = new SimpleDateFormat("MM月dd日", Locale.CHINA);

    /**
     * 如果是今天，显示时分，hh:mm.
     * 如果是昨天，显示昨天
     * 如果是本周，显示周X
     * 如果今年，非本周，显示x月y日
     * 如果非本年，显示x年y月z日
     *
     * @param timestamp the milliseconds since January 1, 1970, 00:00:00 GMT.
     */
    public static String formatTime(long timestamp) {
        // 是否是今天，如果是今天，显示HH:mm
        if (isToday(timestamp)) {
            return format(SDF_HH_MM, timestamp);
        } else {
            // 是否是昨天，如果是昨天，显示昨天
            if (isYesterday(timestamp)) {
                return "昨天";
            } else {
                // 既非今天，也非昨天，如果是同年，看是否同周，如果非同年，显示年月日
                if (isSameYear(timestamp)) {
                    // 如果同周，显示周X
                    if (isSameWeek(timestamp)) {
                        return getWeekDay(timestamp);
                    } else {
                        // 同年不同周，显示月日
                        return formatMMdd(format(SDF_MM_DD, timestamp));
                    }
                } else {
                    // 非同年，显示年月日
                    return formatYYYYMMdd(format(SDF_YYYY_MM_DD, timestamp));
                }
            }
        }
    }

    /**
     * 格式化时间戳
     */
    public static String format(SimpleDateFormat format, long timestamp) {
        if (format != null && timestamp > 0) {
            return format.format(new Date(timestamp));
        }

        return "";
    }

    /**
     * 判断是否为闰年
     */
    private boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    /**
     * 获取某年某月某日是周几
     */
    public static int getWeekIndex(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);

        int index = cal.get(Calendar.DAY_OF_WEEK) - 1;

        return index < 0 ? 0 : index;
    }

    /**
     * 获取某个时间戳是周几
     */
    public static int getWeekIndex(long timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);

        int index = cal.get(Calendar.DAY_OF_WEEK) - 1;

        return index < 0 ? 0 : index;
    }

    /**
     * 获取时间戳对应的年份
     */
    public static int getYear(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取时间戳对应的月份
     */
    public static int getMonth(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获取时间戳对应的月中的日数
     */
    public static int getDate(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取日期，8位数字
     */
    public static int getYyyyMmDd(long timestamp) {
        Date date = new Date(timestamp);
        String time = SDF_yyyyMMdd.format(date);
        return DataUtil.convertToInt(time);
    }

    /**
     * 获取指定时间戳是否是今天
     */
    public static boolean isToday(long timestamp) {
        int day = getYyyyMmDd(timestamp);
        int today = getYyyyMmDd(System.currentTimeMillis());
        return day == today;
    }

    public static boolean isYesterday(long timestamp) {
        Date date = new Date(timestamp);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
        Date today = c.getTime();

        return SDF_yyyyMMdd.format(today).equals(SDF_yyyyMMdd.format(date));
    }

    public static boolean isSameYear(long timestamp) {
        int thisYear = getYear(System.currentTimeMillis());
        int year = getYear(timestamp);
        return thisYear == year;
    }

    public static boolean isSameWeek(long timestamp) {
        int week = getWeekOfYear(timestamp);
        int thisWeek = getWeekOfYear(System.currentTimeMillis());
        return week == thisWeek;
    }

    /**
     * 获取年中星期数
     */
    public static int getWeekOfYear(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);

        int week = calendar.get(Calendar.WEEK_OF_YEAR);

        // 默认周日是每周第一天，但中国习惯周一是第一天，所以把周日的周数减1
        int weekIndex = getWeekIndex(timestamp);
        if (weekIndex == 0) {
            week--;
            week = week < 1 ? 1 : week;
        }

        return week;
    }

    /**
     * 获取时间戳的周数
     * */
    public static String getWeekDay(long timestamp) {
        int weekIndex = getWeekIndex(timestamp);
        if (WEEK_DAYS.length > weekIndex)
            return WEEK_DAYS[weekIndex];

        return "";
    }

    /**
     * 格式化 06月05日为6月5日
     * */
    public static String formatMMdd(String value) {
        String month = value.substring(0, 2);
        String day = value.substring(3, 5);
        month = StringUtil.removePrefix0(month);
        day = StringUtil.removePrefix0(day);
        return month + "月" + day + "日";
    }

    /**
     * 格式化 2016年03月05日 为 2016年3月5日
     * */
    public static String formatYYYYMMdd(String value) {
        String year = value.substring(0, 4);
        String month = value.substring(5, 7);
        String day = value.substring(8, 10);
        year = StringUtil.removePrefix0(year);
        month = StringUtil.removePrefix0(month);
        day = StringUtil.removePrefix0(day);
        return year + "年" + month + "月" + day + "日";
    }

}
