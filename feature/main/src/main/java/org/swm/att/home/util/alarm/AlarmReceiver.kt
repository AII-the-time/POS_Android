package org.swm.att.home.util.alarm

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import java.util.UUID

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val intent = Intent(p0, PreorderAlarmDialog::class.java)
        val preorderDate = p1?.getStringExtra("preorderDate")
        val phoneNumber = p1?.getStringExtra("phoneNumber")
        val totalOrderCount = p1?.getIntExtra("totalOrderCount", -1)
        val preorderId = p1?.getIntExtra("preorderId", -1)
        intent.putExtra("preorderDate", preorderDate)
        intent.putExtra("phoneNumber", phoneNumber)
        intent.putExtra("totalOrderCount", totalOrderCount)
        intent.putExtra("preorderId", preorderId)
        PendingIntent.getActivity(p0, UUID.randomUUID().hashCode(), intent, PendingIntent.FLAG_IMMUTABLE).send()
    }
}