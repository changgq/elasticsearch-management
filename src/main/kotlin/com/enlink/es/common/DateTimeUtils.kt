package com.enlink.es.common

import java.text.SimpleDateFormat
import java.util.*


object CalendarInst {
    val newCal = GregorianCalendar()
}

enum class DateValueEnum {
    YEAR, MONTH, DAY;

    fun vals(): Int = when (this) {
        YEAR -> Calendar.YEAR
        MONTH -> Calendar.MONTH
        DAY -> Calendar.DATE
    }
}

data class DateValue(val value: Int, val unit: DateValueEnum)

fun Any.year(value: Int): DateValue = DateValue(value, DateValueEnum.YEAR)
fun Any.month(value: Int): DateValue = DateValue(value, DateValueEnum.MONTH)
fun Any.day(value: Int): DateValue = DateValue(value, DateValueEnum.DAY)


/**
 * date + year(3)
 * 往后的几天
 */
operator fun Date.plus(nextVal: DateValue): Date {
    val calendar = CalendarInst.newCal
    calendar.time = this
    calendar.add(nextVal.unit.vals(), nextVal.value)
    return calendar.time
}

/**
 * date - month(4)
 */
operator fun Date.minus(nextVal: DateValue): Date {
    val calendar = CalendarInst.newCal
    calendar.time = this
    calendar.add(nextVal.unit.vals(), nextVal.value * -1)
    return calendar.time
}

/**
 * 得到月末
 */
operator fun Date.inc(): Date {
    val calendar = CalendarInst.newCal
    calendar.time = this
    calendar.add(Calendar.MONTH, 1);
    calendar.set(Calendar.DAY_OF_MONTH, 0);
    return calendar.time
}

/**
 * 得到月初
 */
operator fun Date.dec(): Date {
    val calendar = CalendarInst.newCal
    calendar.time = this
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return calendar.time
}


/**
 * 取 年月日时分秒 0 - 5
 * 例如 2015-12-21 22:15:56
 * date[0]:2015  date[1]:12 date[2]:21
 */
operator fun Date.get(position: Int): Int {
    val calendar = GregorianCalendar()
    calendar.time = this
    return when (position) {
        0 -> calendar.get(Calendar.YEAR)
        1 -> calendar.get(Calendar.MONTH)
        2 -> calendar.get(Calendar.DAY_OF_MONTH)
        3 -> calendar.get(Calendar.HOUR)
        4 -> calendar.get(Calendar.MINUTE)
        5 -> calendar.get(Calendar.SECOND)
        else -> 0
    }
}


fun date2ym(day: Date, spliter: String = "-"): String {
    return SimpleDateFormat("yyyy${spliter}MM").format(day)
}

fun date2string(day: Date, spliter: String = "-"): String {
    return SimpleDateFormat("yyyy${spliter}MM${spliter}dd").format(day)
}

fun datetime2string(day: Date, spliter: String = "-"): String {
    return SimpleDateFormat("yyyy${spliter}MM${spliter}dd HH:mm:ss").format(day)
}

/**
 * 当天起始时间
 *
 * @return
 */
fun firstSecond(day: Date): Date {
    val cal = CalendarInst.newCal
    cal.set(day.get(0), day.get(1), day.get(2), 0, 0, 0)
    return cal.time
}

/**
 * 当天起始时间
 *
 * @return
 */
fun lastSecond(day: Date): Date {
    val cal = CalendarInst.newCal
    cal.time = firstSecond(day)
    cal.add(Calendar.DATE, 1)
    cal.add(Calendar.SECOND, -1)
    return cal.time
}

/**
 * 本周第一天
 * @return
 */
fun firstDayOfWeek(): Date {
    val cal = CalendarInst.newCal
    cal.time = Date()
    cal.set(Calendar.DAY_OF_WEEK, 1)
    return cal.time
}

/**
 * 本周最后一天
 * @return
 */
fun lastDayOfWeek(): Date {
    val cal = CalendarInst.newCal
    cal.time = Date()
    cal.set(Calendar.DAY_OF_WEEK, 1)
    cal.roll(Calendar.DAY_OF_WEEK, -1)
    return cal.time
}

fun firstDayOfMonth(): Date {
    var cal = CalendarInst.newCal
    cal.time = Date()
    cal.set(Calendar.DAY_OF_MONTH, 1)
    return cal.time
}

fun lastDayOfMonth(): Date {
    var cal = CalendarInst.newCal
    cal.time = Date()
    cal.set(Calendar.DAY_OF_MONTH, 1)
    cal.roll(Calendar.DAY_OF_MONTH, -1)
    return cal.time
}

fun firstDayOfYear(): Date {
    var cal = CalendarInst.newCal
    cal.time = Date()
    cal.set(Calendar.DAY_OF_YEAR, 1)
    return cal.time
}

fun lastDayOfYear(): Date {
    var cal = CalendarInst.newCal
    cal.time = Date()
    cal.set(Calendar.DAY_OF_YEAR, 1)
    cal.roll(Calendar.DAY_OF_YEAR, -1)
    return cal.time
}

fun firstDayOfThreeMonth(): Date {
    var cal = CalendarInst.newCal
    cal.time = Date()
    cal.add(Calendar.DATE, 90 * -1)
    return cal.time
}

fun firstDayOfSixMonth(): Date {
    var cal = CalendarInst.newCal
    cal.time = Date()
    cal.add(Calendar.DATE, 180 * -1)
    return cal.time
}

fun firstDayOfTwelveMonth(): Date {
    var cal = CalendarInst.newCal
    cal.time = Date()
    cal.add(Calendar.DATE, 365 * -1)
    return cal.time
}