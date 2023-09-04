package org.swm.att.common_ui.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Formatter {
    private val decimalFormat = DecimalFormat("#,###")
    private val baseDateFormatter = SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA)

    fun String.getUnit(): String {
        return if (this.isEmpty()) {
            ""
        } else {
            decimalFormat.format(this.replace(",", "").toDouble())
        }
    }

    fun Date.getBaseFormat(): String {
        return baseDateFormatter.format(this)
    }
}