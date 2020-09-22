package com.example.todotask.model

import java.util.*

class CustomCalendar constructor() {
    private var enteredDate = Calendar.getInstance()
    var diff = 0L
    var diffHours = 0L
    var diffMinutes = 0L

    constructor(calendar: Calendar) : this() {
        val todayDate = Calendar.getInstance()
        enteredDate = calendar
        val millis1 = todayDate.timeInMillis
        val millis2 = enteredDate.timeInMillis
        // Calculate difference in milliseconds
        diff = millis2 - millis1
        diffHours = diff / (60 * 60 * 1000)
        diffMinutes = diff / (60 * 1000) - diffHours * 60
    }


    companion object {
        fun isPassed(date: String, time: String): Boolean {

            val enteredDay = getCalendarFormat(date, time)
            val result = CustomCalendar(enteredDay)
            return when {
                result.diffHours < 0 -> true
                result.diffHours == 0L -> result.diffMinutes < 0
                else -> false
            }
        }

        fun isWithInNearlyDay(date: String, time: String): Boolean {
            val enteredDay = getCalendarFormat(date, time)
            val result = CustomCalendar(enteredDay)
            return when {
                result.diffHours < 72 -> true
                else -> false
            }
        }

        fun isToday(date: String, time: String): Boolean {
            val now = Calendar.getInstance()

            val endOfNow = Calendar.getInstance()
            endOfNow.set(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DATE),
                24,
                0
            )
            val c = CustomCalendar(endOfNow)

            val enteredDay = getCalendarFormat(date, time)
            val result = CustomCalendar(enteredDay)

            return when {
                c.diffHours > result.diffHours -> true
                c.diffHours == result.diffHours -> c.diffMinutes > result.diffMinutes
                else -> false
            }
        }

        fun getCalendarFormat(date: String, time: String): Calendar {
            val calendar = Calendar.getInstance()
            val year = date.substring(0, 4).toInt()
            val month = date.substring(5, 7).toInt() - 1
            val day = date.substring(8).toInt()
            val hour = time.substring(0, 2).toInt()
            val minute = time.substring(3).toInt()
            calendar.set(year, month, day, hour, minute, 0)
            return calendar
        }

        fun getRestTimeMilliSeconds(date: String, time: String): Long {
            val cal = getCalendarFormat(date, time)
            val result = CustomCalendar(cal)
            return result.diff
        }

    }
}
