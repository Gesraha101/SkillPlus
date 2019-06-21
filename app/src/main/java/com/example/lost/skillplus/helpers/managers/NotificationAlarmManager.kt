package com.example.lost.skillplus.helpers.managers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.lost.skillplus.helpers.enums.Actions
import com.example.lost.skillplus.helpers.enums.Keys
import com.example.lost.skillplus.helpers.receivers.AlarmReceiver
import org.joda.time.DateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

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

        fun isValidDate(schedules: ArrayList<Array<Int?>>, date: Array<Int?>, duration: Float): Boolean {
            val dates = convertToLong(schedules)
            val longDate = convertToLong(arrayListOf(date))
            for (time in dates) {
                if (abs(time - longDate[0]) < duration * AlarmManager.INTERVAL_HOUR)
                    return false
            }
            return true
        }

        fun initAlarm(context: Context, fireAt: Long, teacherId: Int, learnerId: Int) {
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val notifyAt = fireAt - AlarmManager.INTERVAL_FIFTEEN_MINUTES * 2
            am.setExact(AlarmManager.RTC_WAKEUP, notifyAt, PendingIntent.getBroadcast(context, Keys.REQUEST_CODE.ordinal, Intent(context, AlarmReceiver::class.java)
                    .setAction(Actions.NOTIFY.action)
                    .addCategory("" + notifyAt), PendingIntent.FLAG_UPDATE_CURRENT))
            am.setExact(AlarmManager.RTC_WAKEUP, fireAt, PendingIntent.getBroadcast(context, Keys.REQUEST_CODE.ordinal, Intent(context, AlarmReceiver::class.java)
                    .setAction(Actions.ALERT.action)
                    .putExtra(Keys.FIRE_DATE.key, fireAt)
                    .putExtra(Keys.TEACHER_ID.key, teacherId)
                    .putExtra(Keys.LEARNER_ID.key, learnerId)
                    .addCategory("" + fireAt), PendingIntent.FLAG_UPDATE_CURRENT))
        }

        fun cancelAlarm(context: Context, timeStamp: Long) {
            val pendingIntent = PendingIntent.getBroadcast(context, Keys.REQUEST_CODE.ordinal, Intent(context, AlarmReceiver::class.java)
                    .setAction(Actions.NOTIFY.name)
                    .addCategory("" + timeStamp), PendingIntent.FLAG_UPDATE_CURRENT)
            val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            am.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}