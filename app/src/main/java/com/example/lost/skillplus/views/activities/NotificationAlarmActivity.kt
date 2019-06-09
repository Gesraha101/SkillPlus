package com.example.lost.skillplus.views.activities

import android.app.NotificationManager
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.example.lost.skillplus.R

class NotificationAlarmActivity : AppCompatActivity() {

    private lateinit var alertBuilder: AlertDialog.Builder
    private val notificationManager: NotificationManager? = null
    private lateinit var player: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_alarm)
        setFinishOnTouchOutside(false)

        player = MediaPlayer.create(this, R.raw.notification)
        player.isLooping = true // Set looping
        player.setVolume(100f, 100f)
        player.start()
        val tripId = intent.getIntExtra("tripid", 0)
        alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Skill Plus")
                .setMessage("Time for your session!")
                .setPositiveButton("Start") { _, _ ->
                    player.stop()
                    player.release()
                    startActivity(Intent(this@NotificationAlarmActivity, SessionActivity::class.java))
                    finish()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    player.stop()
                    player.release()
                    finish()
                }
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .show()
    }
}
