package org.swm.att.common_ui.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Formatter {
    private val decimalFormat = DecimalFormat("#,###")
    private val baseDateFormatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)
    private val today = Calendar.getInstance().apply {
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    fun getStringBaseCurrencyUnit(currency: String): String {
        return if (currency.isEmpty()) {
            ""
        } else {
            decimalFormat.format(currency.replace(",", "").toDouble())
        }
    }

    fun getDateBaseFormattingResult(date: Date): String {
        return baseDateFormatter.format(date)
    }

    fun isToday(date: Date): Boolean {
        return date == today
    }
}