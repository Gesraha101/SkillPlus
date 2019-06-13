package com.example.lost.skillplus.models.managers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.lost.skillplus.models.enums.Actions
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.receivers.AlarmReceiver
import org.joda.time.DateTime
import java.util.*
import kotlin.collections.ArrayList

class NotificationAlarmManager {

    companion object {

        fun convertToLong(dateList: ArrayList<Array<Int?>>): ArrayList<Long> {
            val arr: ArrayList<Long> = ArrayList()
            for (date in dateList) {
                var time = System.currentTimeMillis() - TimeZone.getDefault().getOffset(System.currentTimeMillis()) - System.currentTimeMillis() % AlarmManager.INTERVAL_DAY + (date[0]!! - DateTime().dayOfWeek) * AlarmManager.INTERVAL_DAY + date[1]!! * AlarmManager.INTERVAL_HOUR + date[2]!! * (AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15)
                if (date[0]!! < DateTime().dayOfWeek || time < System.currentTimeMillis()) {
                    time += 7 * AlarmManager.INTERVAL_DAY
                }
                arr += time
            }
            return arr
        }

        fun initAlarm(context: Context, fireAt: Long) {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val notifyAt = fireAt - AlarmManager.INTERVAL_FIFTEEN_MINUTES / 3
                am.setExact(AlarmManager.RTC_WAKEUP, notifyAt, PendingIntent.getBroadcast(context, Keys.REQUEST_CODE.ordinal, Intent(context, AlarmReceiver::class.java).setAction(Actions.NOTIFY.action).addCategory("" + notifyAt), PendingIntent.FLAG_UPDATE_CURRENT))
                am.setExact(AlarmManager.RTC_WAKEUP, fireAt, PendingIntent.getBroadcast(context, Keys.REQUEST_CODE.ordinal, Intent(context, AlarmReceiver::class.java).setAction(Actions.ALERT.action).putExtra(Keys.FIRE_DATE.key, fireAt).addCategory("" + fireAt), PendingIntent.FLAG_UPDATE_CURRENT))
            }
        }

        fun cancelAlarm(context: Context, timeStamp: Long) {
            val pendingIntent = PendingIntent.getBroadcast(context, Keys.REQUEST_CODE.ordinal, Intent(context, AlarmReceiver::class.java).setAction(Actions.NOTIFY.name).addCategory("" + timeStamp), PendingIntent.FLAG_UPDATE_CURRENT)
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            am.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}