package org.swm.att.common_ui.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Formatter {
    private val decimalFormat = DecimalFormat("#,###")
    private val baseDateFormatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
    private val baseSimpleDateFormatter = SimpleDateFormat("yy.MM.dd", Locale.KOREA)
    private val baseTimeFormatter = SimpleDateFormat("HH시 mm분", Locale.KOREA)
    private val baseDateTimeFormatter = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분", Locale.KOREA)
    private val baseDateTimeStringFormatter =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.KOREA)
    private val today = Calendar.getInstance().apply {
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    fun getDateFromString(date: String): Date {
        return baseDateTimeStringFormatter.parse(date)
    }

    fun getSimpleStringFromDate(date: Date): String {
        return baseSimpleDateFormatter.format(date)
    }

    fun getTimeFromString(date: Date): String {
        return baseTimeFormatter.format(date)
    }

    fun getStringBaseCurrencyUnit(currency: String?): String {
        return if (currency.isNullOrEmpty()) {
            ""
        } else {
            decimalFormat.format(currency.replace(",", "").toDouble())
        }
    }

    fun getBaseStringFromCurrencyUnit(currency: String?): String {
        return if (currency.isNullOrEmpty()) {
            ""
        } else {
            decimalFormat.parse(currency).toString()
        }
    }

    fun getDateBaseFormattingResult(date: Date): String {
        return baseDateFormatter.format(date)
    }

    fun <T> getDataTimeBaseFormattingResult(date: T?): String {
        date?.let {
            return if (date is String) {
                val date = baseDateTimeStringFormatter.parse(date)
                baseDateTimeFormatter.format(date)
            } else {
                baseDateTimeFormatter.format(date)
            }
        }
        return ""
    }

    fun isToday(date: Date): Boolean {
        return date == today
    }

    fun getStringByDateTimeBaseFormatter(date: Date): String {
        return baseDateTimeStringFormatter.format(date)
    }
}