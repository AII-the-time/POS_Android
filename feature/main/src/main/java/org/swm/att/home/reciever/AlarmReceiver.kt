package org.swm.att.home.reciever

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.swm.att.common_ui.presenter.alarm.PreorderAlarmDialog

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        val intent = Intent(p0, PreorderAlarmDialog::class.java)
        PendingIntent.getActivity(p0, 0, intent, PendingIntent.FLAG_IMMUTABLE).send()
    }
}