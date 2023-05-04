package com.kcx.common.utils.date;

/**
 * 时间戳
 */
public class UnixUtils {
    /**
     * 获取当前时间时间戳，精确到秒
     */
    public static Integer currentTimeFormatUnix() {
        long time = System.currentTimeMillis() / 1000;
        return Integer.valueOf(time + "");
    }

    /**
     * 获取当日凌晨时间戳，精确到秒
     */
    public static Integer currentDateFormatUnix() {
        int time = currentTimeFormatUnix();
        return time - (time + 28800) % 86400;
    }

    /**
     * 获取本周凌晨时间戳，精确到秒
     */
    public static Integer currentWeekFormatUnix() {
        int time = currentDateFormatUnix();
        return time - ((time - 86400 * 3) % (86400 * 7) / 86400) * 86400;
    }
}
