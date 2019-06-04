package com.example.lost.skillplus.models.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.views.activities.SessionActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val i = Intent(context, SessionActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        i.putExtra(Keys.FIRE_DATE.key, intent!!.getIntExtra(Keys.FIRE_DATE.key, 0))
        context!!.startActivity(i)
    }
}