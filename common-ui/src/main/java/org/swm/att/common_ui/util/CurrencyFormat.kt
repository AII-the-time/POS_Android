package org.swm.att.common_ui.util

import java.text.DecimalFormat

object CurrencyFormat {
    private val decimalFormat = DecimalFormat("#,###")

    fun String.getUnit(): String {
        return if (this.isEmpty()) {
            ""
        } else {
            decimalFormat.format(this.replace(",", "").toDouble())
        }
    }
}