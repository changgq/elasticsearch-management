package com.enlink.es.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 *
 * @author changgq
 */
public class DateUtils {

    /**
     * 获取昨天的日期
     *
     * @return
     */
    public static Date getYesterday() {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public static Date plus(Date day, int days) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(day);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    public static Date minus(Date day, int days) {
        return plus(day, days * -1);
    }

    /**
     * 获取当前日期的月份
     *
     * @return {yyyy-MM}
     */
    public static String getMonth() {
        return DateUtils.date2monthstring(new Date());
    }

    /**
     * 获取当前日期的年份
     *
     * @return {yyyy}
     */
    public static String getYear() {
        return DateUtils.date2yearstring(new Date());
    }

    /**
     * 日期转化为字符串
     *
     * @param day
     * @return {yyyy-MM-dd}
     */
    public static String date2string(Date day) {
        return new SimpleDateFormat("yyyy-MM-dd").format(day);
    }

    /**
     * 日期转化为月份
     *
     * @param day
     * @return {yyyy-MM}
     */
    public static String date2monthstring(Date day) {
        return new SimpleDateFormat("yyyy-MM").format(day);
    }

    /**
     * 日期转化为年份
     *
     * @param day
     * @return {yyyy}
     */
    public static String date2yearstring(Date day) {
        return new SimpleDateFormat("yyyy").format(day);
    }

    /**
     * 日期时间格式转化为字符串
     *
     * @param day
     * @return {yyyy-MM-dd HH:mm:ss}
     */
    public static String datetime2string(Date day) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(day);
    }

    /**
     * 字符串转化为日期
     *
     * @param day {yyyy-MM-dd}
     * @return
     * @throws ParseException
     */
    public static Date string2date(String day) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(day);
    }

    /**
     * 字符串转化为日期时间
     *
     * @param day {yyyy-MM-dd HH:mm:ss}
     * @return
     * @throws ParseException
     */
    public static Date string2datetime(String day) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(day);
    }

    /**
     * 本周第一天的日期
     *
     * @return
     */
    public static Date firstDayOfWeek(Date day) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(day == null ? new Date() : day);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        return cal.getTime();
    }

    /**
     * 本周最后一天的日期
     *
     * @return
     */
    public static Date lastDayOfWeek(Date day) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(day == null ? new Date() : day);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        cal.roll(Calendar.DAY_OF_WEEK, -1);
        return cal.getTime();
    }

    /**
     * 月份第一天
     *
     * @return
     */
    public static Date firstDayOfMonth(Date day) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(day == null ? new Date() : day);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }

    /**
     * 月份最后一天
     *
     * @return
     */
    public static Date lastDayOfMonth(Date day) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(day == null ? new Date() : day);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    /**
     * 年份第一天
     *
     * @return
     */
    public static Date firstDayOfYear(Date day) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(day == null ? new Date() : day);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        return cal.getTime();
    }

    /**
     * 年份最后一天
     *
     * @return
     */
    public static Date lastDayOfYear(Date day) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(day == null ? new Date() : day);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.roll(Calendar.DAY_OF_YEAR, -1);
        return cal.getTime();
    }

    /**
     * 最近三个月的第一天
     *
     * @return
     */
    public static Date firstDayOfLast3Month() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 90 * -1);
        return cal.getTime();
    }

    /**
     * 最近六个月的第一天
     *
     * @return
     */
    public static Date firstDayOfLast6Month() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 180 * -1);
        return cal.getTime();
    }

    /**
     * 最近十二个月的第一天
     *
     * @return
     */
    public static Date firstDayOfLast12Month() {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, 365 * -1);
        return cal.getTime();
    }
}
