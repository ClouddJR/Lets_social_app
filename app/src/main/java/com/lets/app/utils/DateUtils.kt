package com.lets.app.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun formatDate(year: Int, month: Int, day: Int): String {
        val formattedMonth = if (month < 10) {
            "0$month"
        } else {
            "$month"
        }

        val formattedDay = if (day < 10) {
            "0$day"
        } else {
            "$day"
        }

        return "$year-$formattedMonth-$formattedDay"
    }

    fun formatTime(hour: Int, minute: Int): String {
        val formattedHour = if (hour < 10) {
            "0$hour"
        } else {
            "$hour"
        }

        val formattedMinutes = if (minute < 10) {
            "0$minute"
        } else {
            "$minute"
        }

        return "$formattedHour:$formattedMinutes"
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateFromString(dateString: String): Date {
        val df = SimpleDateFormat("MM/dd/yyyy")
        return df.parse(dateString)
    }

}