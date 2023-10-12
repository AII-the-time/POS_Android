package org.swm.att.home.util.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import org.swm.att.common_ui.util.Formatter
import org.swm.att.common_ui.util.getUTCDateTime
import java.util.Calendar
import java.util.UUID
import java.util.concurrent.TimeUnit

object AlarmManager {
    private val pendingIntentList = mutableListOf<PendingIntent>()

    fun setPreorderAlarm(
        context: Context,
        preorderDate: String,
        phoneNumber: String,
        totalOrderCount: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val currentTime = Calendar.getInstance().time.getUTCDateTime()
        val alarmTime = Formatter.getDateFromString(preorderDate)
        if (alarmTime.after(currentTime)) {
            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            alarmIntent.putExtra("preorderDate", preorderDate)
            alarmIntent.putExtra("phoneNumber", phoneNumber)
            alarmIntent.putExtra("totalOrderCount", totalOrderCount)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                UUID.randomUUID().variant(),
                alarmIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
            pendingIntentList.add(pendingIntent)
            alarmManager.setExact(
                AlarmManager.RTC,
                alarmTime.time - TimeUnit.MINUTES.toMillis(10),
                pendingIntent
            )
        }
    }

    fun cancelAllAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        pendingIntentList.forEach {
            alarmManager.cancel(it)
        }
    }
}