package org.swm.att.common_ui.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.swm.att.common_ui.presenter.alarm.PreorderAlarmDialog

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val intent = Intent(p0, PreorderAlarmDialog::class.java)
        val preorderDate = p1?.getStringExtra("preorderDate")
        val phoneNumber = p1?.getStringExtra("phoneNumber")
        val totalOrderCount = p1?.getIntExtra("totalOrderCount", -1)
        intent.putExtra("preorderDate", preorderDate)
        intent.putExtra("phoneNumber", phoneNumber)
        intent.putExtra("totalOrderCount", totalOrderCount)
        PendingIntent.getActivity(p0, 0, intent, PendingIntent.FLAG_IMMUTABLE).send()
    }
}