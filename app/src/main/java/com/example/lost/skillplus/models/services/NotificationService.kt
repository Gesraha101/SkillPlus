package com.example.lost.skillplus.models.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.JobIntentService
import android.support.v4.app.NotificationCompat
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.Actions
import com.example.lost.skillplus.models.enums.Ids
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.views.activities.NotificationActivity
import com.example.lost.skillplus.views.activities.SessionActivity

class NotificationService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        if (intent.action == Actions.NOTIFY.action) {
            val date = intent.getLongExtra(Keys.FIRE_DATE.key, 0)
            val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val builder: NotificationCompat.Builder

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(Ids.NOTIFICATION_CHANNEL.id, Ids.VISIBLE.id, NotificationManager.IMPORTANCE_DEFAULT)
                channel.description = "Displays reminders for upcoming sessions"
                manager.createNotificationChannel(channel)
                builder = NotificationCompat.Builder(this, Ids.NOTIFICATION_CHANNEL.id)
            } else
                builder = NotificationCompat.Builder(this)

            builder.setContentTitle("Session Reminder")
                    .setContentText("You have an upcoming session now")
                    .setAutoCancel(false)
                    .setSmallIcon(R.drawable.ic_today_notification)
                    .setContentIntent(PendingIntent.getActivity(this, notificationID, Intent(this, SessionActivity::class.java).putExtra(Keys.FIRE_DATE.key, date).setAction(Actions.SHOW.action).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK), PendingIntent.FLAG_UPDATE_CURRENT))

            manager.notify(notificationID++, builder.build())

        }
    }

    companion object {

        private var notificationID = 0

        fun enqueueTask(context: Context, work: Intent) {
            enqueueWork(context, NotificationService::class.java, Ids.JOB.ordinal, work)
        }
    }
}