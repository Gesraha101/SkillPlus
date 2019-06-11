package com.example.lost.skillplus.views.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.enums.Keys

class NotificationAlarmActivity : AppCompatActivity() {

    private lateinit var alertBuilder: AlertDialog.Builder
    private lateinit var player: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_alarm)
        setFinishOnTouchOutside(false)
        val fireDate = intent.getLongExtra(Keys.FIRE_DATE.key, 0)
        player = MediaPlayer.create(this, R.raw.alarm)
        player.isLooping = true // Set looping
        player.setVolume(100f, 100f)
        player.start()
        alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setTitle("Skill Plus")
                .setMessage("Time for your session!")
                .setPositiveButton("Start") { _, _ ->
                    player.stop()
                    player.release()
                    startActivity(Intent(this@NotificationAlarmActivity, SessionActivity::class.java).putExtra(Keys.FIRE_DATE.key, fireDate.toString()))
                    finish()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    player.stop()
                    player.release()
                    finish()
                }
                .setIcon(R.drawable.alarm_icon)
                .setCancelable(false)
                .show()
    }
}
