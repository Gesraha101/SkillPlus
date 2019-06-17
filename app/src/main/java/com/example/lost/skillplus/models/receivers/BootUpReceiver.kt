package com.example.lost.skillplus.models.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.lost.skillplus.models.managers.NotificationAlarmManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.Schedule
import com.google.gson.Gson


class BootUpReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val timestamps = PreferencesManager(context).getSchedules()
            if (timestamps != null) {
                for (timestamp in timestamps) {
                    val schedule = Gson().fromJson(timestamp, Schedule::class.java)
                    if (schedule.isMentor)
                        NotificationAlarmManager.initAlarm(context, schedule.date, PreferencesManager(context).getId(), schedule.otherId!!)
                    else
                        NotificationAlarmManager.initAlarm(context, schedule.date, schedule.otherId!!, PreferencesManager(context).getId())
                }
            }
        }
    }

}