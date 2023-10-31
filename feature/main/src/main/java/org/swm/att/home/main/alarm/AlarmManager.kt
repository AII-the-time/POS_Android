package org.swm.att.home.main.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import org.swm.att.common_ui.util.Formatter
import org.swm.att.common_ui.util.getUTCDateTime
import java.util.Calendar
import java.util.concurrent.TimeUnit

object AlarmManager {
    private val pendingIntentList = mutableMapOf<Int, PendingIntent>()

    fun setPreorderAlarm(
        context: Context,
        preorderDate: String,
        phoneNumber: String,
        totalOrderCount: Int,
        preorderId: Int
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val currentTime = Calendar.getInstance().time.getUTCDateTime()
        val alarmTime = Formatter.getDateFromString(preorderDate)
        if (alarmTime.after(currentTime)) {
            val alarmIntent = Intent(context, AlarmReceiver::class.java)
            alarmIntent.putExtra("preorderDate", preorderDate)
            alarmIntent.putExtra("phoneNumber", phoneNumber)
            alarmIntent.putExtra("totalOrderCount", totalOrderCount)
            alarmIntent.putExtra("preorderId", preorderId)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                preorderId,
                alarmIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
            pendingIntentList[preorderId] = pendingIntent
            alarmManager.setExact(
                AlarmManager.RTC,
                alarmTime.time - TimeUnit.MINUTES.toMillis(10),
                pendingIntent
            )
        }
    }

    fun cancelAllAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        pendingIntentList.values.forEach {
            alarmManager.cancel(it)
        }
    }

    fun cancelAlarm(context: Context, preorderId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        pendingIntentList[preorderId]?.let {
            alarmManager.cancel(it)
        }
    }

    fun updateAlarm(
        context: Context,
        preorderDate: String,
        phoneNumber: String,
        totalOrderCount: Int,
        preorderId: Int
    ) {
        cancelAlarm(context, preorderId)
        setPreorderAlarm(context, preorderDate, phoneNumber, totalOrderCount, preorderId)
    }
}