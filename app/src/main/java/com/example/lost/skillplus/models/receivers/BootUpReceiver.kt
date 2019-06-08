package com.example.lost.skillplus.models.receivers

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.managers.NotificationAlarmManager


class BootUpReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val timestamps = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(Keys.TIMESTAMPS.key, null)
            if (timestamps != null)
                for (timestamp in timestamps)
                    NotificationAlarmManager.initAlarm(context, timestamp.toLong() - AlarmManager.INTERVAL_FIFTEEN_MINUTES * 2, timestamp.toLong())
        }
    }

}