package com.kcx.common.utils.date;

import com.kcx.common.exception.CustomException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 日期处理
 */
public class DateUtils {
    /**
     * 时间格式(yyyy-MM-dd)
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 时间格式(yyyy-MM-dd HH:mm:ss)
     */
    public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * Date 转 String
     * @param date 日期必填
     * @param pattern 日期格式,默认返回 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        if (date == null) {
            return null;
        } else {
            return new SimpleDateFormat(StringUtils.isBlank(pattern) ? DATE_TIME_PATTERN: pattern).format(date);
        }
    }

    /**
     * String 转 Date
     * @param date
     * @param pattern 日期格式,默认返回 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date stringToDate(String date, String pattern) {
        if (StringUtils.isBlank(date)) {
            return null;
        } else {
            DateTimeFormatter fmt = DateTimeFormat.forPattern(StringUtils.isBlank(pattern) ? DATE_TIME_PATTERN : pattern);
            return fmt.parseLocalDateTime(date).toDate();
        }
    }

    /**
     * 获取当天的开始时间
     * @param date
     * @return 2023-04-24 00:00:00
     */
    public static String getStartDate(Date date) {
        Calendar instance = Calendar.getInstance();
        if (null != date) {
            instance.setTime(date);
            instance.set(Calendar.HOUR_OF_DAY, 0);
            instance.set(Calendar.MINUTE, 0);
            instance.set(Calendar.SECOND, 0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(instance.getTime());
        } else {
            return null;
        }
    }

    /**
     * 获取当天的结束时间
     * @param date
     * @return 2023-04-24 23:59:59
     */
    public static String getEndDate(Date date) {
        if (null != date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(calendar.getTime());
        } else {
            return null;
        }
    }

    /**
     * 获取当月第一天
     * @param date
     * @param pattern 日期格式,默认返回 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getMonthStartTime(Date date, String pattern) {

        Long currentTime = System.currentTimeMillis();
        java.time.format.DateTimeFormatter ftf = java.time.format.DateTimeFormatter.ofPattern(StringUtils.isBlank(pattern) ? DATE_TIME_PATTERN : pattern);
        Calendar calendar = Calendar.getInstance(); // 获取当前日期
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        calendar.setTimeInMillis(currentTime);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1); // 设置为1号,当前日期既为本月第一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return ftf.format(
                LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(calendar.getTimeInMillis()), ZoneId.systemDefault()));
    }

    /**
     * 获取当月最后一天
     * @param date
     * @param pattern 日期格式,默认返回 yyyy-MM-dd HH:mm:ss
     * @return 2023-04-30 23:59:59
     */
    public static String getTheMonthEndTime(Date date, String pattern) {
        java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ofPattern(
                StringUtils.isBlank(pattern) ? DATE_TIME_PATTERN : pattern);
        Calendar calendar = Calendar.getInstance(); // 获取当前日期
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 0);
        calendar.add(Calendar.MONTH, 0);
        //获取当前月最后一天
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return dateTimeFormatter.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(
                calendar.getTimeInMillis()), ZoneId.systemDefault()));
    }

    /**
     * 获取距离当前时间某一周的开始日期、结束日期
     * @param date 当前时间
     * @param week 周期 -2上上周， -1上周，0本周，1下周，2下下周
     * @return 返回date[0]周开始日期、date[1]周结束日期
     */
    public static Date[] getWeekStartAndEnd(Date date,int week) {
        DateTime dateTime = new DateTime(date);
        LocalDate localDate = new LocalDate(dateTime.plusWeeks(week));
        localDate = localDate.dayOfWeek().withMinimumValue();
        Date beginDate = localDate.toDate();
        Date endDate = localDate.plusDays(6).toDate();
        return new Date[]{beginDate, endDate};
    }

    /**
     * 对日期进行加/减
     * @param date 日期
     * @param value 具体值，正数是加，负数是减
     * @param unit 单位Seconds，Minutes，Hours，Days，Weeks，Months，Years
     * @return
     */
    public static Date operationDate(Date date, int value,String unit) {
        DateTime dateTime = new DateTime(date);
        switch (unit){
            case "Seconds":
                dateTime = dateTime.plusSeconds(value);
                break;
            case "Minutes":
                dateTime = dateTime.plusMinutes(value);
                break;
            case "Hours":
                dateTime = dateTime.plusHours(value);
                break;
            case "Days":
                dateTime = dateTime.plusDays(value);
                break;
            case "Weeks":
                dateTime = dateTime.plusWeeks(value);
                break;
            case "Months":
                dateTime = dateTime.plusMonths(value);
                break;
            case "Years":
                dateTime = dateTime.plusYears(value);
                break;
            default:
                throw new CustomException("时间单位格式错误");
        }
        return dateTime.toDate();
    }

    /**
     * 比较两个字符串时间大小
     * @param time1
     * @param time2
     * @return 1：time1>time2,0:time1=time2,-1:time1<time2
     */
    public static int compareDate(String time1, String time2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
        int flagValue = 0;
        try {
            Date date1, date2;
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);
            long millisecond = date1.getTime() - date2.getTime();
            if (millisecond > 0) {
                flagValue = 1;
            } else if (millisecond < 0) {
                flagValue = -1;
            } else if (millisecond == 0) {
                flagValue = 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flagValue;
    }

    /**
     * 比较两个Date时间大小
     * @param time1
     * @param time2
     * @return 1：time1>time2,0:time1=time2,-1:time1<time2
     */
    public static int compareDate(Date time1, Date time2) {
        int flagValue = 0;
        try {
            long millisecond = time1.getTime() - time2.getTime();
            if (millisecond > 0) {
                flagValue = 1;
            } else if (millisecond < 0) {
                flagValue = -1;
            } else if (millisecond == 0) {
                flagValue = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (flagValue);
    }

    /**
     * 计算两个时间相差的时间
     * @param time1
     * @param time2
     * @param unit 单位Seconds，Minutes，Hours，Days，Weeks
     * @return 一个正整数
     */
    public static int diffDate(String time1, String time2,String unit) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
        Long diff = 0L;
        try {
            Date date1 = simpleDateFormat.parse(time1);
            Date date2 = simpleDateFormat.parse(time2);
            long millisecond = date2.getTime() - date1.getTime();
            switch (unit){
                case "Seconds":
                    diff = millisecond / 1000;
                    break;
                case "Minutes":
                    diff = millisecond / (60 * 1000);
                    break;
                case "Hours":
                    diff = millisecond / (60 * 60 * 1000);
                    break;
                case "Days":
                    diff = millisecond / (24 * 60 * 60 * 1000);
                    break;
                case "Weeks":
                    diff = millisecond / (7 * 24 * 60 * 60 * 1000);
                    break;
                default:
                    throw new CustomException("时间单位格式错误");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Math.abs(diff.intValue());
    }

    /**
     * 计算两个时间相差的时间
     * @param time1
     * @param time2
     * @param unit 单位Seconds，Minutes，Hours，Days，Weeks
     * @return 一个正整数
     */
    public static int diffDate(Date time1, Date time2,String unit) {
        Long diff = 0L;
        long millisecond = time2.getTime() - time1.getTime();
        switch (unit){
            case "Seconds":
                diff = millisecond / 1000;
                break;
            case "Minutes":
                diff = millisecond / (60 * 1000);
                break;
            case "Hours":
                diff = millisecond / (60 * 60 * 1000);
                break;
            case "Days":
                diff = millisecond / (24 * 60 * 60 * 1000);
                break;
            case "Weeks":
                diff = millisecond / (7 * 24 * 60 * 60 * 1000);
                break;
            default:
                throw new CustomException("时间单位格式错误");
        }
        return Math.abs(diff.intValue());
    }


    /**
     * 人性化显示时间
     * @param date
     * @return xxx秒前，xxx分钟前，xxx小时前...
     */
    public static String showTime(Date date) {
        String r = "";
        if (date == null) {
            return r;
        }
        long nowMillis = System.currentTimeMillis();
        long dateMillis = date.getTime();
        long diff = Math.abs(nowMillis - dateMillis);
        if (diff < 60000) {// 一分钟内
            long seconds = diff / 1000;
            if (seconds < 5) {
                r = "刚刚";
            } else {
                r = seconds + "秒前";
            }
        } else if (diff >= 60000 && diff < 3600000) {
            // 1小时内
            r = diff / 60000 + "分钟前";
        } else if (diff >= 3600000 && diff < 86400000) {
            // 1天内
            r = diff / 3600000 + "小时前";
        } else if (diff >= 86400000 && diff < 2592000000L) {
            // 30天内
            r = diff / 86400000 + "天前";
        } else {// 日期格式
            SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
            r = df.format(date);
        }
        return r;
    }

    /**
     * 根据对应语言-显示对应格式
     * @param languageCode 国际语言编码：美国en 中国zh
     * @param date 日期
     * @return Apr 24, 2023/2023-4-24
     */
    public static String formatDateByObject(String languageCode, Object date) {
        if (StringUtils.isBlank(languageCode) || date == null || StringUtils.isBlank(date.toString())) {
            return "";
        } else {
            return DateFormat.getDateInstance(DateFormat.MEDIUM, new Locale(languageCode)).format(date);
        }
    }


    /**
     * 将 Date 转 LocalDateTime
     * @param date
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        //atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * LocalDateTime 转 Date
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * String 转 LocalDateTime
     * @param date
     * @param pattern 日期格式,默认返回 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static LocalDateTime stringToLocalDateTime(String date, String pattern) {
        if (StringUtils.isBlank(date)) {
            return null;
        } else {
            return LocalDateTime.parse(date, java.time.format.DateTimeFormatter.ofPattern(
                    StringUtils.isBlank(pattern) ? DATE_TIME_PATTERN : pattern));
        }
    }

    /**
     * LocalDateTime 转 String
     * @param localDateTime
     * @param pattern
     * @return
     */
    public static String localDateTimeToString(LocalDateTime localDateTime, String pattern) {
        return localDateTime.format(java.time.format.DateTimeFormatter.ofPattern(StringUtils.isBlank(pattern) ? "yyyy-MM-dd HH:mm:ss" : pattern));
    }


    /**
     * 计算两个LocalDateTime之间相差的时间
     * @param localDateTime1
     * @param localDateTime2
     * @param unit 单位Seconds，Minutes，Hours，Days，Weeks
     * @return 一个正整数
     */
    public static long diffLocalDateTime(LocalDateTime localDateTime1, LocalDateTime localDateTime2,String unit) {
        Duration duration = Duration.between(localDateTime1, localDateTime2);
        switch (unit) {
            case "Seconds":
                return duration.toMillis();
            case "Minutes":
                return duration.toMinutes();
            case "Hours":
                return duration.toHours();
            case "Days":
                return duration.toDays();
            case "Weeks":
                return duration.toDays() / 7;
            default:
                throw new CustomException("时间单位格式错误");
        }
    }

    /**
     * 生成时间轴
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param simpleDateFormat 时间格式
     * @param type 类型 month按月,week按周,day按天,hour按小时
     * @return 按月结果2022-01, 2022-02... 按周结果2022-01-10, 2022-01-16... 按天结果2022-01-05, 2022-01-06... 按小时结果2022-01-09 01:00, 2022-01-09 02:00
     * @throws java.text.ParseException
     * @author kcx
     * @example generateTimeline("2022-01-05","2022-02-28", new SimpleDateFormat("yyyy-MM-dd"),"day")
     * @example generateTimeline("2022-01-09","2022-02-28", new SimpleDateFormat("yyyy-MM"),"month")
     * @example generateTimeline("2022-01-09","2022-02-28", new SimpleDateFormat("yyyy-MM-dd"),"week")
     * @example generateTimeline("2022-01-09","2022-01-09", new SimpleDateFormat("yyyy-MM-dd HH:mm"),"hour")
     */
    public static List<String> generateTimeline(String startTime, String endTime, SimpleDateFormat simpleDateFormat, String type) throws ParseException {
        List<String> list = new ArrayList<>();
        //开始时间的年、月、日
        int startYear;
        int startMonth;
        int startDay;
        //结束时间的年、月、日
        int endYear;
        int endMonth;
        int endDay;

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(simpleDateFormat.parse(startTime+" 00:00:00"));

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(simpleDateFormat.parse(endTime+" 00:00:00"));

        startYear = startDate.get(Calendar.YEAR);
        startMonth = startDate.get(Calendar.MONTH) + 1;
        startDay = startDate.get(Calendar.DAY_OF_MONTH);

        endYear = endDate.get(Calendar.YEAR);
        endMonth = endDate.get(Calendar.MONTH) + 1;
        endDay = endDate.get(Calendar.DAY_OF_MONTH);
        //按月
        if (type.equals("month")) {
            //年
            for (int i = startYear; i <= endYear; i++) {
                int begin_month = 1;
                int end_month = 12;
                //第一次
                if (i == startYear) {
                    begin_month = startMonth;
                }
                //最后一次
                if (i == endYear) {
                    end_month = endMonth;
                }
                //月
                for (int j = begin_month; j <= end_month; j++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, i);
                    calendar.set(Calendar.MONTH, j - 1);
                    list.add(simpleDateFormat.format(calendar.getTime()));
                }
            }
        }
        //按周
        if (type.equals("week")) {
            //加入起始时间
            boolean joinFirstDay=true;
            //加入所有的周日、周一
            while(startDate.getTime().getTime()<endDate.getTime().getTime()){
                //1周日 2周一
                if(joinFirstDay||startDate.get(Calendar.DAY_OF_WEEK)==2||startDate.get(Calendar.DAY_OF_WEEK)==1){
                    if(startDate.get(Calendar.DAY_OF_WEEK)== 1 && list.size() == 0){
                        list.add(simpleDateFormat.format(startDate.getTime()));
                    }
                    list.add(simpleDateFormat.format(startDate.getTime()));
                    joinFirstDay = false;
                }
                //起始时间+1天
                startDate.add(Calendar.DAY_OF_MONTH, 1);
            }
            //加入结束时间
            list.add(simpleDateFormat.format(endDate.getTime()));
            if(list.size()%2!=0){
                list.add(simpleDateFormat.format(endDate.getTime()));
            }
        }
        //按天
        if (type.equals("day")) {
            //年
            for (int i = startYear; i <= endYear; i++) {
                int begin_month = 1;
                int end_month = 12;
                //第一次
                if (i == startYear) {
                    begin_month = startMonth;
                }
                //最后一次
                if (i == endYear) {
                    end_month = endMonth;
                }
                //月
                for (int j = begin_month; j <= end_month; j++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, i);
                    calendar.set(Calendar.MONTH, j - 1);
                    int begin_day = 1;
                    int end_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    if (j == startMonth) {
                        begin_day = startDay;
                    }
                    if (j == endMonth) {
                        end_day = endDay;
                    }
                    //日
                    for (int k = begin_day; k <= end_day; k++) {
                        calendar.set(Calendar.DAY_OF_MONTH, k);
                        list.add(simpleDateFormat.format(calendar.getTime()));
                    }
                }
            }
        }
        //按时
        if (type.equals("hour")) {
            //年
            for (int i = startYear; i <= endYear; i++) {
                int begin_month = 1;
                int end_month = 12;
                //第一次
                if (i == startYear) {
                    begin_month = startMonth;
                }
                //最后一次
                if (i == endYear) {
                    end_month = endMonth;
                }
                //月
                for (int j = begin_month; j <= end_month; j++) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, i);
                    calendar.set(Calendar.MONTH, j - 1);
                    int begin_day = 1;
                    int end_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    if (j == startMonth) {
                        begin_day = startDay;
                    }
                    if (j == endMonth) {
                        end_day = endDay;
                    }
                    //日
                    for (int k = begin_day; k <= end_day; k++) {
                        calendar.set(Calendar.DAY_OF_MONTH, k);
                        //时
                        for (int n = 1; n <= 24; n++) {
                            calendar.set(Calendar.HOUR_OF_DAY, n);
                            calendar.set(Calendar.MINUTE, 0);
                            list.add(simpleDateFormat.format(calendar.getTime()));
                        }
                    }
                }
            }
        }
        return list;
    }

}
