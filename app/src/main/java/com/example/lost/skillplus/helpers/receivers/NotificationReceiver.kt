package com.example.lost.skillplus.helpers.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.example.lost.skillplus.helpers.enums.Actions
import com.example.lost.skillplus.helpers.services.NotificationService


class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (isOnline(context)) {
            val i = Intent(context, NotificationService::class.java)
            i.action = Actions.CHECK.action
            NotificationService.enqueueTask(context, i)
        }
    }

    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}