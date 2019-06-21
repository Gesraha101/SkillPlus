package com.example.lost.skillplus.helpers.services

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.lost.skillplus.helpers.enums.Actions
import com.example.lost.skillplus.helpers.enums.Ids


class NotificationScheduler : JobService() {

    override fun onStartJob(params: JobParameters): Boolean {
        val service = Intent(applicationContext, NotificationService::class.java).setAction(Actions.CHECK.action)
        NotificationService.enqueueTask(this@NotificationScheduler, service)
        scheduleJob(applicationContext, 30 * 1000)
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        return true
    }

    companion object {

        fun scheduleJob(context: Context, period: Long?) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val serviceComponent = ComponentName(context, NotificationScheduler::class.java)
                val builder = JobInfo.Builder(Ids.JOB.ordinal, serviceComponent)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .setPersisted(true)
                if (period != null)
                    builder.setMinimumLatency(period)
                val jobScheduler = context.getSystemService(JobScheduler::class.java)
                jobScheduler.schedule(builder.build())
            }
        }
    }

}