package com.active.orbit.baseapp.core.utils

import android.text.TextUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility class that provides some useful methods to manage timestamps
 *
 * @author omar.brugna
 */
@Suppress("MemberVisibilityCanBePrivate")
object TimeUtils {

    const val ONE_SECOND_MILLIS = 1000L
    const val ONE_MINUTE_MILLIS = 60L * ONE_SECOND_MILLIS
    const val ONE_HOUR_MILLIS = 60L * ONE_MINUTE_MILLIS
    const val ONE_DAY_MILLIS = 24L * ONE_HOUR_MILLIS

    private val utcTimezone = TimeZone.getTimeZone("UTC")
    private val defaultTimezone = TimeZone.getDefault()

    fun getUTC(): Calendar {
        return toUTC(getCurrent())
    }

    fun getCurrent(): Calendar {
        return Calendar.getInstance()
    }

    fun getCurrent(timeInMillis: Long): Calendar {
        val currentCalendar = getCurrent()
        currentCalendar.timeInMillis = timeInMillis
        return currentCalendar
    }

    fun fromUTC(date: String?): Calendar? {
        return parse(date, Constants.DATE_FORMAT_UTC)
    }

    fun toUTC(calendar: Calendar): Calendar {
        val utcCalendar = getCurrent()
        utcCalendar.timeZone = calendar.timeZone
        utcCalendar.timeInMillis = calendar.timeInMillis
        utcCalendar.timeZone = utcTimezone
        return utcCalendar
    }

    fun toDefault(calendar: Calendar): Calendar {
        val defaultCalendar = getCurrent()
        defaultCalendar.timeZone = calendar.timeZone
        defaultCalendar.timeInMillis = calendar.timeInMillis
        defaultCalendar.timeZone = defaultTimezone
        return defaultCalendar
    }

    fun getZeroSeconds(fromCalendar: Calendar? = null): Calendar {
        val calendar = fromCalendar ?: getCurrent()
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar
    }

    fun format(calendar: Calendar, toFormat: String): String {
        try {
            val simpleDateFormat = SimpleDateFormat(toFormat, Locale.getDefault())
            simpleDateFormat.calendar = calendar
            return simpleDateFormat.format(calendar.time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Constants.EMPTY
    }

    fun parse(date: String?, fromFormat: String): Calendar? {
        if (TextUtils.isEmpty(date)) {
            Logger.e("Trying to parse an empty date string")
            return null
        }
        try {
            val simpleDateFormat = SimpleDateFormat(fromFormat, Locale.getDefault())
            simpleDateFormat.timeZone = utcTimezone
            val dateParsed = simpleDateFormat.parse(date!!)
            if (dateParsed != null) {
                val calendar = getCurrent()
                calendar.time = dateParsed
                return calendar
            } else Logger.e("Parsed date is null $date")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * @param date date in String
     * @param fromFormat input date format
     * @param toFormat output date format
     * @return the formatted date
     */
    fun convertDate(date: String, fromFormat: String, toFormat: String): String {
        try {
            val calendar = parse(date, fromFormat)
            if (calendar != null) return format(calendar, toFormat)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Constants.EMPTY
    }

    private fun getCurrentDay(): Int {
        return getCurrent().get(Calendar.DAY_OF_YEAR)
    }

    private fun getCurrentMonth(): Int {
        return getCurrent().get(Calendar.MONTH)
    }

    private fun getCurrentYear(): Int {
        return getCurrent().get(Calendar.YEAR)
    }

    fun getDayOfMonth(calendar: Calendar): Int {
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun isToday(calendar: Calendar?): Boolean {
        return isThisMonth(calendar) && calendar?.get(Calendar.DAY_OF_YEAR) == getCurrentDay()
    }

    fun isThisMonth(calendar: Calendar?): Boolean {
        return isThisYear(calendar) && calendar?.get(Calendar.MONTH) == getCurrentMonth()
    }

    fun isThisYear(calendar: Calendar?): Boolean {
        return calendar?.get(Calendar.YEAR) == getCurrentYear()
    }

    fun isSameDay(calendarStart: Calendar?, calendarEnd: Calendar?): Boolean {
        return calendarStart?.get(Calendar.DAY_OF_YEAR) == calendarEnd?.get(Calendar.DAY_OF_YEAR) &&
                calendarStart?.get(Calendar.MONTH) == calendarEnd?.get(Calendar.MONTH) &&
                calendarStart?.get(Calendar.YEAR) == calendarEnd?.get(Calendar.YEAR)
    }

    fun calculateSuffix(day: Int): String {
        val calculateDay = day % 10
        return if (calculateDay == 1) "st" else if (calculateDay == 2) "nd" else if (calculateDay == 3) "rd" else "th"
    }

    fun formatHHMMSS(seconds: Long): String {
        val secondsCount = seconds % 60
        var secondsString = secondsCount.toString()
        val minutesCount = seconds / 60 % 60
        var minutesString = minutesCount.toString()
        val hoursCount = seconds / 60 / 60
        var hoursString = hoursCount.toString()
        secondsString = secondsString.padStart(2, '0')
        minutesString = minutesString.padStart(2, '0')
        hoursString = hoursString.padStart(2, '0')
        return "$hoursString:$minutesString:$secondsString"
    }
}