package org.swm.att.common_ui.util

import java.util.Calendar
import java.util.Date

fun Date.getUTCDateTime(): Date {
    val localTime = Calendar.getInstance()
    localTime.time = this
    localTime.add(Calendar.HOUR, -9)
    return localTime.time
}

fun Date.getRTCDateTime(): Date {
    val localTime = Calendar.getInstance()
    localTime.time = this
    localTime.add(Calendar.HOUR, 9)
    return localTime.time
}

fun Date.getRTCDateTimeFromData(data: Date): Date {
    val localTime = Calendar.getInstance()
    localTime.time = data
    localTime.add(Calendar.HOUR, 9)
    return localTime.time
}

fun String?.getValueOrNull(): String? {
    return if (this.isNullOrEmpty()) {
        null
    } else {
        this
    }
}