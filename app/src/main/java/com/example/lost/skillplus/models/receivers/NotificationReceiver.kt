package com.example.lost.skillplus.models.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.lost.skillplus.models.services.NotificationService
import android.net.ConnectivityManager
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.podos.raw.Notification
import com.example.lost.skillplus.models.podos.responses.PostsResponse
import retrofit2.Call


class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (isOnline(context)) {
            val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
            val call: Call<Notification>? = service?.getNotifications(1)
            NotificationService.enqueueTask(context, intent)
        }
    }

    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        //should check null because in airplane mode it will be null
        return netInfo != null && netInfo.isConnected
    }
}