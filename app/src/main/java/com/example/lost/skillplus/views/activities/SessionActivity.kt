package com.example.lost.skillplus.views.activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.helpers.enums.Keys
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration


class SessionActivity : AppCompatActivity() {


    private lateinit var key: String

    private val LOG_TAG = SessionActivity::class.java.simpleName
    private var learnerId: Int? = null
    private var date: Long? = null
    private val PERMISSION_REQ_ID = 22

    // permission WRITE_EXTERNAL_STORAGE is not mandatory for Agora RTC SDK, just in case if you wanna save logs to external sdcard
    private val REQUESTED_PERMISSIONS = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private var mRtcEngine: RtcEngine? = null
    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
            runOnUiThread { setupRemoteVideo(uid) }
        }

        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread { onRemoteUserLeft() }
        }

        override fun onUserMuteVideo(uid: Int, muted: Boolean) {
            runOnUiThread { onRemoteUserVideoMuted(uid, muted) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)
        learnerId = intent!!.getStringExtra(Keys.LEARNER_ID.key).toInt()
        date = intent!!.getStringExtra(Keys.FIRE_DATE.key).toLong()
        key = intent!!.getStringExtra(Keys.LEARNER_ID.key) + intent!!.getStringExtra(Keys.FIRE_DATE.key) + intent!!.getStringExtra(Keys.TEACHER_ID.key)
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            initAgoraEngineAndJoinChannel(key)
        }
    }

    private fun initAgoraEngineAndJoinChannel(channelId: String) {
        initializeAgoraEngine()
        setupVideoProfile()
        setupLocalVideo()
        joinChannel(channelId)
    }

    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        Log.i(LOG_TAG, "checkSelfPermission $permission $requestCode")
        if (ContextCompat.checkSelfPermission(this,
                        permission) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    REQUESTED_PERMISSIONS,
                    requestCode)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        Log.i(LOG_TAG, "onRequestPermissionsResult " + grantResults[0] + " " + requestCode)

        when (requestCode) {
            PERMISSION_REQ_ID -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED || grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                    showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO + "/" + Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    finish()
                }

                initAgoraEngineAndJoinChannel(key)
            }
        }
    }

    private fun showLongToast(msg: String) {
        this.runOnUiThread { Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show() }
    }

    override fun onDestroy() {
        super.onDestroy()

        leaveChannel()
        RtcEngine.destroy()
        mRtcEngine = null
    }

    fun onLocalVideoMuteClicked(view: View) {
        val iv = view as ImageView
        if (iv.isSelected) {
            iv.isSelected = false
            iv.clearColorFilter()
        } else {
            iv.isSelected = true
            iv.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
        }

        mRtcEngine!!.muteLocalVideoStream(iv.isSelected)

        val container = findViewById<View>(R.id.local_video_view_container) as FrameLayout
        val surfaceView = container.getChildAt(0) as SurfaceView
        surfaceView.setZOrderMediaOverlay(!iv.isSelected)
        surfaceView.visibility = if (iv.isSelected) View.GONE else View.VISIBLE
    }

    fun onLocalAudioMuteClicked(view: View) {
        val iv = view as ImageView
        if (iv.isSelected) {
            iv.isSelected = false
            iv.clearColorFilter()
        } else {
            iv.isSelected = true
            iv.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
        }

        mRtcEngine!!.muteLocalAudioStream(iv.isSelected)
    }

    fun onSwitchCameraClicked(view: View) {
        mRtcEngine!!.switchCamera()
    }

    override fun onBackPressed() {
    }

    fun onEndCallClicked(view: View) {
        finish()
        /*if (PreferencesManager(this@SessionActivity).getId() == learnerId) {
            val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
            val call: Call<PostSessionResponse>? = service?.doPostSkillSession(Session(learnerId!!, date!!))
            call?.enqueue(object : Callback<PostSessionResponse> {

                override fun onResponse(call: Call<PostSessionResponse>, response: Response<PostSessionResponse>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status) {
                            rating_overlay.visibility = View.VISIBLE
                            instructor_rate.setOnRatingBarChangeListener { _: SimpleRatingBar, fl: Float, _: Boolean ->
                                val rateCall: Call<PostSessionResponse>? = service.rateSkillMentor(Rate(response.body()!!.skill_id!!, fl))
                                rateCall?.enqueue(object : Callback<PostSessionResponse> {

                                    override fun onResponse(call: Call<PostSessionResponse>, response: Response<PostSessionResponse>) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(this@SessionActivity, "Thank you for your feedback", Toast.LENGTH_SHORT).show()
                                            finish()
                                        }
                                    }

                                    override fun onFailure(call: Call<PostSessionResponse>, t: Throwable) {
                                        Toast.makeText(this@SessionActivity, "Failed" + t.localizedMessage, Toast.LENGTH_LONG).show()
                                    }
                                })
                            }
                        } else {
                            finish()
                        }
                    }
                }

                override fun onFailure(call: Call<PostSessionResponse>, t: Throwable) {
                    Toast.makeText(this@SessionActivity, "Failed" + t.localizedMessage, Toast.LENGTH_LONG).show()
                }
            })
        } else {
            finish()
        }*/
    }

    private fun initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(baseContext, getString(R.string.agora_app_id), mRtcEventHandler)

        } catch (e: Exception) {
            Log.e(LOG_TAG, Log.getStackTraceString(e))

            throw RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e))
        }

    }

    private fun setupVideoProfile() {
        mRtcEngine!!.enableVideo()

        //      mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false); // Earlier than 2.3.0
        mRtcEngine!!.setVideoEncoderConfiguration(VideoEncoderConfiguration(VideoEncoderConfiguration.VD_1920x1080, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT))
    }

    private fun setupLocalVideo() {
        val container = findViewById<View>(R.id.local_video_view_container) as FrameLayout
        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        surfaceView.setZOrderMediaOverlay(true)
        container.addView(surfaceView)
        mRtcEngine!!.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0))
    }

    private fun joinChannel(channelId: String) {
        mRtcEngine!!.joinChannel(null, channelId, "Extra Optional Data", 0) // if you do not specify the uid, we will generate the uid for you
    }

    private fun setupRemoteVideo(uid: Int) {
        val container = findViewById<View>(R.id.remote_video_view_container) as FrameLayout

        if (container.childCount >= 1) {
            return
        }

        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        container.addView(surfaceView)
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid))

        surfaceView.tag = uid // for mark purpose
    }

    private fun leaveChannel() {
        mRtcEngine!!.leaveChannel()
    }

    private fun onRemoteUserLeft() {
        val container = findViewById<View>(R.id.remote_video_view_container) as FrameLayout
        container.removeAllViews()
    }

    private fun onRemoteUserVideoMuted(uid: Int, muted: Boolean) {
        val container = findViewById<View>(R.id.remote_video_view_container) as FrameLayout

        val surfaceView = container.getChildAt(0) as SurfaceView

        val tag = surfaceView.tag
        if (tag != null && tag as Int == uid) {
            surfaceView.visibility = if (muted) View.GONE else View.VISIBLE
        }
    }
}
