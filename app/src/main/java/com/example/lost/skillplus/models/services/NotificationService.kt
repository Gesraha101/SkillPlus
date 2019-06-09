package com.example.lost.skillplus.models.services

import RetrofitManager
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.JobIntentService
import android.support.v4.app.NotificationCompat
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.Actions
import com.example.lost.skillplus.models.enums.Headers
import com.example.lost.skillplus.models.enums.Ids
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.NotificationAlarmManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.NotificationsRequest
import com.example.lost.skillplus.models.podos.responses.NotificationsResponse
import com.example.lost.skillplus.views.activities.HomeActivity
import com.example.lost.skillplus.views.activities.NotificationAlarmActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationService : JobIntentService() {

    private lateinit var context: Context

    @TargetApi(Build.VERSION_CODES.O)
    fun createChannel(manager: NotificationManager) {
        val channel = NotificationChannel(Ids.NOTIFICATION_CHANNEL.id, Ids.VISIBLE.id, NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "Displays reminders for upcoming sessions"
        manager.createNotificationChannel(channel)
    }

    private fun notify(manager: NotificationManager, header: String, body: String, intent: PendingIntent?) {
        val builder = NotificationCompat.Builder(this, Ids.NOTIFICATION_CHANNEL.id)
                .setContentTitle(header)
                .setContentText(body)
                .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
                .setAutoCancel(false)
                .setSmallIcon(R.drawable.ic_today_notification)
                .setContentIntent(intent)

        manager.notify(notificationID++, builder.build())
    }

    fun generateNotification(manager: NotificationManager, header: String, body: String, intent: PendingIntent?) {
        createChannel(manager)
        notify(manager, header, body, intent)
    }

    override fun onHandleWork(intent: Intent) {
        context = this
        when {
            intent.action == Actions.NOTIFY.action -> {
                val body = "You have an upcoming session in 30 minutes"
                generateNotification(this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager, Headers.SESSION.header, body, null)
            }
            intent.action == Actions.ALERT.action -> startActivity(Intent(this, NotificationAlarmActivity::class.java))
            intent.action == Actions.CHECK.action -> {
                val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
                val call: Call<NotificationsResponse>? = service?.getNotifications(NotificationsRequest(PreferencesManager(this).getId(), PreferencesManager(this).getLastUpdated()))
                call?.enqueue(object : Callback<NotificationsResponse> {

                    override fun onResponse(call: Call<NotificationsResponse>, response: Response<NotificationsResponse>) {
                        if (response.isSuccessful) {
                            if (response.body()?.status  == true) {
                                for (notification in response.body()!!.notifications) {
                                    val alarmIntent = Intent(context, HomeActivity::class.java)
                                    alarmIntent.putExtra(Keys.NOTIFICATIONS.key, response.body()!!.notifications)
                                    val alarmPendingIntent = PendingIntent.getActivity(context, Keys.REQUEST_CODE.ordinal, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                                    if (notification.skill_name != null) {
                                        val body = "You have new notifications. Tab to view"
                                        for (i in 0 until notification.schedule!!.size)
                                            NotificationAlarmManager.initAlarm(context, notification.schedule[i])
                                        generateNotification(getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager, Headers.NOTIFICATION.header, body, alarmPendingIntent)
                                    }
                                }
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