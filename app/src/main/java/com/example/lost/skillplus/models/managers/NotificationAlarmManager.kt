package com.example.lost.skillplus.models.managers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.lost.skillplus.models.enums.Actions
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.receivers.NotificationReceiver

class NotificationAlarmManager {
    companion object {

        fun initAlarm(context: Context, fireAt: Long) {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            am.setExact(AlarmManager.RTC_WAKEUP, fireAt, PendingIntent.getBroadcast(context, Keys.REQUEST_CODE.ordinal, Intent(context, NotificationReceiver::class.java).setAction(Actions.NOTIFY.action).putExtra(Keys.FIRE_DATE.key, fireAt).addCategory("" + fireAt), PendingIntent.FLAG_UPDATE_CURRENT))
        }
    }
}