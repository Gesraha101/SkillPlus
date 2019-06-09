package com.example.lost.skillplus.models.services

import RetrofitManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.JobIntentService
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.Actions
import com.example.lost.skillplus.models.enums.Ids
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.responses.NotificationsResponse
import com.example.lost.skillplus.views.activities.NotificationAlarmActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationService : JobIntentService() {


    override fun onHandleWork(intent: Intent) {
        when {
            intent.action == Actions.NOTIFY.action -> {
                val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(Ids.NOTIFICATION_CHANNEL.id, Ids.VISIBLE.id, NotificationManager.IMPORTANCE_DEFAULT)
                    channel.description = "Displays reminders for upcoming sessions"
                    manager.createNotificationChannel(channel)
                }

                val builder = NotificationCompat.Builder(this, Ids.NOTIFICATION_CHANNEL.id)
                        .setContentTitle("Session Reminder")
                        .setContentText("You have an upcoming session in 30 minutes")
                        .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
                        .setAutoCancel(false)
                        .setSmallIcon(R.drawable.ic_launcher_background)

                manager.notify(notificationID++, builder.build())

            }
            intent.action == Actions.ALERT.action -> startActivity(Intent(this, NotificationAlarmActivity::class.java))
            intent.action == Actions.CHECK.action -> {
                val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
                val call: Call<NotificationsResponse>? = service?.getNotifications(PreferencesManager(this).getId(), System.currentTimeMillis())
                call?.enqueue(object : Callback<NotificationsResponse> {

                    override fun onResponse(call: Call<NotificationsResponse>, response: Response<NotificationsResponse>) {
                        if (response.isSuccessful) {
                            if(response.body()?.status  == true) {

                            }
                        } else {

                        }
                    }

                    override fun onFailure(call: Call<NotificationsResponse>, t: Throwable) {
                        Toast.makeText(this@NotificationService, "Failed" + t.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    companion object {

        private var notificationID = 0

        fun enqueueTask(context: Context, work: Intent) {
            enqueueWork(context, NotificationService::class.java, Ids.JOB.ordinal, work)
        }
    }
}