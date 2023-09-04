package org.swm.att.common_ui.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatter {
    private val decimalFormat = DecimalFormat("#,###")
    private val baseDateFormatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)

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
}