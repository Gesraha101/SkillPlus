package com.example.lost.skillplus.views.activities

import android.app.job.JobScheduler
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.Ids
import com.example.lost.skillplus.models.services.NotificationScheduler

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (getSystemService(JobScheduler::class.java).getPendingJob(Ids.JOB.ordinal) == null)
                NotificationScheduler.scheduleJob(this@SplashActivity, null)
        }

        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 1000)
    }
}
