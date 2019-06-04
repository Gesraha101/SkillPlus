package com.example.lost.skillplus.views.activities

import android.content.DialogInterface
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.example.lost.skillplus.R

class NotificationAlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_alarm)
        setFinishOnTouchOutside(false)
//        alarmPresenter = AlarmPresenterImpl(this)
//
//        player = MediaPlayer.create(this, R.raw.notification)
//        player.setLooping(true) // Set looping
//        player.setVolume(100f, 100f)
//        player.start()
//
//        val tripId = intent.getIntExtra("tripid", 0)
//        tripDTO = MyAppDB.getAppDatabase(this).tripDao().getTrip(tripId)
//        alertBuilder = AlertDialog.Builder(this)
//        alertBuilder.setTitle("Skill Plus")
//                .setMessage("You have an upcoming session in 30 minutes")
//                .setPositiveButton("start", DialogInterface.OnClickListener { dialogInterface, i ->
//                    player.stop()
//                    player.release()
//                    alarmPresenter.startTrip(tripDTO, tripId)
//                    finish()
//                })
//                .setNegativeButton("Cancel Trip", DialogInterface.OnClickListener { dialog, which ->
//                    player.stop()
//                    player.release()
//                    alarmPresenter.cancelTrip(tripDTO, tripId)
//                    finish()
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setCancelable(false)
//                .show()
//
//    }
    }
}
