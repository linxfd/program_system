package com.program.utils.date;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author linxf
 * @date 2024/8/9
 */
@Slf4j
public class DateUtil {

    // 以毫秒表示的时间
    private static final long DAY_IN_MILLIS = 24 * 3600 * 1000L;
    private static final long HOUR_IN_MILLIS = 3600 * 1000L;
    private static final long MINUTE_IN_MILLIS = 60 * 1000L;
    private static final long SECOND_IN_MILLIS = 1000L;

    /**
     * 获取时间格式化对象
     * @param dateFormatEnum
     * @return
     */
    public static SimpleDateFormat obvSimpleDateFormat(DateFormatEnum dateFormatEnum) {
        return new SimpleDateFormat(dateFormatEnum.getFormateStr());
    }

    /**
     * 当前日期
     *
     * @return 系统当前时间
     */
    public static Date getDateNow() {
        return new Date();
    }

    /**
     * 字符串转换成日期
     * @param str
     * @param sdf
     * @return
     */
    public static Date strToDate(String str, SimpleDateFormat sdf) {
        if (null == str || "".equals(str)) {
            return null;
        }
        Date date = null;
        try {
            date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 日期转换为字符串
     * @param date
     * @param sdf
     * @return
     */
    public static String dateToStr(Date date, SimpleDateFormat sdf) {
        if (null == date) {
            return null;
        }
        return sdf.format(date);
    }

    /**
     * 系统时间的毫秒数
     *
     * @return 系统时间的毫秒数
     */
    public static long getMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 指定日期的毫秒数
     *
     * @param date 指定日期
     * @return 指定日期的毫秒数
     */
    public static long getMillis(Date date) {
        return date.getTime();
    }

    /**
     * date转时间戳
     * @param date
     * @return
     */
    public static Timestamp dateToTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * date转日历
     * @param date
     * @return
     */
    public static Calendar dateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    // ////////////////////////////////////////////////////////////////////////////
    // dateDiff
    // 计算两个日期之间的差值
    // ////////////////////////////////////////////////////////////////////////////

    /**
     * 计算两个时间之间的差值，根据标志的不同而不同
     *
     * @param flag   计算标志，表示按照年/月/日/时/分/秒等计算
     * @param dateSrc 减数
     * @param dateDes 被减数
     * @return 两个日期之间的差值
     */
    @SuppressWarnings("static-access")
    public static int dateDiff(char flag, Date dateSrc, Date dateDes) {
        Calendar calSrc = dateToCalendar(dateSrc);
        Calendar calDes = dateToCalendar(dateDes);

        long millisDiff = getMillis(dateSrc) - getMillis(dateDes);
        if (flag == 'y') {
            return (calSrc.get(calSrc.YEAR) - calDes.get(calDes.YEAR));
        }

        if (flag == 'd') {
            return (int) (millisDiff / DAY_IN_MILLIS);
        }

        if (flag == 'h') {
            return (int) (millisDiff / HOUR_IN_MILLIS);
        }

        if (flag == 'm') {
            return (int) (millisDiff / MINUTE_IN_MILLIS);
        }

        if (flag == 's') {
            return (int) (millisDiff / SECOND_IN_MILLIS);
        }
        return 0;
    }

    public static int getYear() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(getDateNow());
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 根据指点年月获取当前月开始时间
     * @param year 年
     * @param month 月
     * @return
     */
    public static Date getMonthBeginTime(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate localDate = yearMonth.atDay(1);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        ZonedDateTime zonedDateTime = startOfDay.atZone(ZoneId.of("Asia/Shanghai"));

        return Date.from(zonedDateTime.toInstant());
    }


    /**
     * 根据指点年月获取当前月结束时间
     * @param year 年
     * @param month 月
     * @return
     */
    public static Date getMonthEndTime(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();
        LocalDateTime localDateTime = endOfMonth.atTime(23, 59, 59, 999);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * 获取当前天的起始时间：当天0点0分0秒
     */
    public static Date getDayStartTime(Date date) {
        Calendar day = dateToCalendar(date);
        day.set(Calendar.HOUR_OF_DAY, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MILLISECOND, 0);
        return day.getTime();
    }

    /**
     * 获取今天的起始时间：今天的0点0分0秒
     * @return
     */
    public static Date getTodayStartTime() {
        return getDayStartTime(new Date());
    }

    /**
     * 获取当前天的结束时间：当天的23点59分59秒
     */
    public static Date getDayEndTime(Date date) {
        Calendar day = dateToCalendar(date);
        day.set(Calendar.HOUR_OF_DAY, 23);
        day.set(Calendar.MINUTE, 59);
        day.set(Calendar.SECOND, 59);
        day.set(Calendar.MILLISECOND, 999);
        return day.getTime();
    }

    /**
     * 获取今天的结束时间：今天的23点59分59秒
     * @return
     */
    public static Date getTodayEndTime() {
        return getDayEndTime(new Date());
    }

    /**
     * 根据date参数返回ISO8601时间格式
     * @param date
     * @return
     */
    public static String getISO8601DateString(Date date) {
        SimpleDateFormat sdf = obvSimpleDateFormat(DateFormatEnum.SDF_YMDHMS);
        String timestamp = dateToStr(date, sdf);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dt1 = LocalDateTime.parse(timestamp,formatter);
        ZoneOffset offset = ZoneOffset.of("+08:00");
        OffsetDateTime dateTime = OffsetDateTime.of(dt1,offset);

        DateTimeFormatter formatter2=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        timestamp = dateTime.format(formatter2);
        return timestamp;
    }

    /**
     * 时间追加年
     * @param date Date对象
     * @param num 要加的年数，可为负数
     * @return
     */
    public static Date getTimeAddYear(Date date, Integer num){
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, num);
            return calendar.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 时间追加月
     * @param date Date对象
     * @param num 要加的月数，可为负数
     * @return
     */
    public static Date getTimeAddMonth(Date date, Integer num){
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, num);
            return calendar.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 时间追加天
     * @param date Date对象
     * @param num 要加的天数，可为负数
     * @return
     */
    public static Date getTimeAddDay(Date date, Integer num){
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, num);
            return calendar.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 时间追加小时
     * @param date Date对象
     * @param num 要加的小时数，可为负数
     * @return
     */
    public static Date getTimeAddHour(Date date, Integer num){
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, num);
            return calendar.getTime();// new Date()为获取当前系统时间
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 时间追加分
     * @param date Date对象
     * @param num 要加的分钟数，可为负数
     * @return
     */
    public static Date getTimeAddMinutes(Date date, Integer num){
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, num);
            return calendar.getTime();
        } catch (Exception e) {
            return null;
        }
    }

}
