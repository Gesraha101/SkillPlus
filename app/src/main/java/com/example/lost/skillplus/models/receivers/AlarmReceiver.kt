package com.example.lost.skillplus.models.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.services.NotificationService

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val i = Intent(context, NotificationService::class.java)
        i.action = intent!!.action
        i.putExtra(Keys.FIRE_DATE.key, intent.getIntExtra(Keys.FIRE_DATE.key, 0))
        NotificationService.enqueueTask(context!!, i)
    }
}