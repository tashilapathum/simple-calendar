package com.tashila.simplecalendar

import android.content.Context
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.timepicker.MaterialTimePicker
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class Utils {
    companion object {

        fun getTodayDate(): String {
            val localDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
            return formatter.format(localDate)
        }

        /**Returns date formatted as dd/MM/yyyy */
        fun getDate(millis: Long): String {
            return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }

        /**Returns LocalDate object */
        fun getLocalDate(millis: Long): LocalDate {
            return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

        /**Returns time formatted as HH:mm */
        fun getTime(millis: Long): String {
            return Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalTime()
                .format(DateTimeFormatter.ofPattern("HH:mm"))
        }

        /**Returns time formatted as HH:mm from a time picker*/
        fun getTime(timePicker: MaterialTimePicker): String {
            val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
            return formatter.format(LocalTime.of(timePicker.hour, timePicker.minute))
        }

        fun getTimeInMillis(timePicker: MaterialTimePicker): Long {
            val localTime = LocalTime.of(timePicker.hour, timePicker.minute)
            return localTime.toNanoOfDay()
        }

        fun isValid(vararg tils: TextInputLayout): Boolean {
            for (til in tils) {
                val text = til.editText!!.text.toString().trim()
                if (text.isEmpty()) {
                    til.error = "Required"
                    return false
                } else til.error = null
            }
            return true
        }

        fun getDayAfter(daysToAdd: Long): Long {
            return LocalDate.now()
                .plusDays(daysToAdd)
                .atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli()
        }
    }
}