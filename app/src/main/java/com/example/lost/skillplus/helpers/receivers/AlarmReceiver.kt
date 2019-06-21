package com.example.lost.skillplus.helpers.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.lost.skillplus.helpers.enums.Keys
import com.example.lost.skillplus.helpers.services.NotificationService

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val i = Intent(context, NotificationService::class.java)
        i.action = intent!!.action
        i.putExtra(Keys.FIRE_DATE.key, intent.getLongExtra(Keys.FIRE_DATE.key, 0))
        NotificationService.enqueueTask(context!!, i)
    }
}