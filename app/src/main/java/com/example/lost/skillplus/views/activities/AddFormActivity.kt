package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.ScheduleAdapter
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.NotificationAlarmManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.DayTime
import com.example.lost.skillplus.models.podos.raw.Form
import com.example.lost.skillplus.models.podos.responses.FormResponse
import kotlinx.android.synthetic.main.activity_add_form.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AddFormActivity : AppCompatActivity() {
    private lateinit var mAdapter: ScheduleAdapter

    var dayPicked: Int? = null
    var hourPicked: Int? = null
    var minutePicked: Int? = null
    var isEmpty: Boolean? = true
    var dayTimeList: ArrayList<DayTime> = arrayListOf()
    var dayTimeArray = arrayListOf<Array<Int?>>()

    @RequiresApi(Build.VERSION_CODES.M)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_form)
        setSupportActionBar(toolbar)

        var progressOverlay : View = findViewById(R.id.progress_overlay)

        rV_Schedule.apply {
            layoutManager = LinearLayoutManager(this@AddFormActivity)
            mAdapter = ScheduleAdapter(dayTimeList)
            rV_Schedule.adapter = mAdapter
        }
        val adapter = ArrayAdapter.createFromResource(this, R.array.week_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.setSelection(0)
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position>0)
                    dayPicked = position
                else
                    dayPicked = null
            }
        }
        val hours = findViewById<TextView>(R.id.eT_Hours)
        hours.text="Tap to set time"
        hours.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(this@AddFormActivity, R.style.TimePickerTheme,
                    TimePickerDialog.OnTimeSetListener(function = { _, h, m ->
                        hours.text = h.toString() + ":" + m.toString()
                        hourPicked = h
                        minutePicked = m
                    }), hour, minute, true)
            var date = Calendar.getInstance()
            tpd.updateTime(date.time.hours,date.time.minutes)
            tpd.show()

        }

        btn_add_to_schedule.setOnClickListener {
            if (dayPicked == null || hourPicked == null || minutePicked == null) {
                Toast.makeText(this, "Please set a date first!", Toast.LENGTH_LONG).show()
            } else {
                if (isEmpty == true) {
                    btn_add_need.visibility = View.VISIBLE
                    isEmpty = false
                }
                dayTimeList.add(DayTime(spinner.selectedItem.toString(), hourPicked, minutePicked))
                mAdapter.notifyDataSetChanged()
                dayTimeArray.add(arrayOf(dayPicked, hourPicked, minutePicked))
                mScrollView.post { mScrollView.fullScroll(ScrollView.FOCUS_DOWN) }

                //Empty the fields
                hours.text="Tap to set time"
                hourPicked=null
                minutePicked=null
                spinner.setSelection(0)
                dayPicked=null
            }
        }

        val shake = AnimationUtils.loadAnimation(this, R.anim.animation) as Animation
        var badEntry: Boolean

        btn_add_need.setOnClickListener {
                 badEntry = false
                if (eT_NumberOfSessions.text.toString().toIntOrNull() == null) {
                    eT_NumberOfSessions.error = "A number is required"
                    eT_NumberOfSessions.startAnimation(shake)
                    eT_NumberOfSessions.requestFocus()
                    badEntry = true
                }
                if (eT_SessionDuration.text.toString().toIntOrNull() == null) {
                    eT_SessionDuration.error = "A number is required"
                    eT_SessionDuration.startAnimation(shake)
                    eT_SessionDuration.requestFocus()
                    badEntry = true
                }
                if (eT_Price.text.toString().toFloatOrNull() == null) {
                    eT_Price.error = "A number is required"
                    eT_Price.startAnimation(shake)
                    eT_Price.requestFocus()
                    badEntry = true
                }
                if (eT_ExtraFees.text.toString().toFloatOrNull() == null) {
                    eT_ExtraFees.error = "A number is required"
                    eT_ExtraFees.startAnimation(shake)
                    eT_ExtraFees.requestFocus()
                    badEntry = true
                }
                if (!badEntry) {
                    progressOverlay.visibility = View.VISIBLE
                    animateView(progressOverlay, View.VISIBLE, 0.4f, 200)
                    var form = Form(
                            eT_NumberOfSessions.text.toString().toInt(),
                            eT_SessionDuration.text.toString().toFloat(),
                            eT_Price.text.toString().toFloat(),
                            eT_ExtraFees.text.toString().toFloat(),
                            intent.getIntExtra("need_id",0),
                            NotificationAlarmManager.convertToLong(dayTimeArray),
                            PreferencesManager(this@AddFormActivity).getId())

                    val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
                    val call: Call<FormResponse>? = service?.addForm(form)
                    call?.enqueue(object : Callback<FormResponse> {
                        override fun onResponse(call: Call<FormResponse>, response: Response<FormResponse>) {
                            if (response.isSuccessful) {
                                if (response.body()?.status == true) {
                                    for (date in form.schedule!!)
                                        NotificationAlarmManager.initAlarm(this@AddFormActivity, date)
                                    val i = Intent(this@AddFormActivity, HomeActivity::class.java)
                                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    Handler().postDelayed({
                                        animateView(progressOverlay, View.GONE, 0f, 200);
                                        startActivity(i)
                                        finish()
                                        Snackbar.make(it, "Added Successfully !", Snackbar.LENGTH_INDEFINITE).show()
                                    }, 2500)

                                } else {
                                    Toast.makeText(this@AddFormActivity, "Failed1", Toast.LENGTH_LONG).show()

                                }
                            } else {
                                Toast.makeText(this@AddFormActivity, "Failed2", Toast.LENGTH_LONG).show()

                                //Received response but not "OK" response i.e error in the request sent (Server can't handle this request)
                            }
                        }

                        override fun onFailure(call: Call<FormResponse>, t: Throwable) {
                            Toast.makeText(this@AddFormActivity, t.message, Toast.LENGTH_LONG).show()
                            //Error receiving response from server i.e error in podo received (Retrofit can't handle this response)
                        }

                    })
                }

            }
        }
    fun animateView(view: View, toVisibility: Int, toAlpha: Float, duration: Int) {
        val show = toVisibility == View.VISIBLE
        if (show) {
            view.alpha = 0f
        }
        view.visibility = View.VISIBLE
        view.animate()
                .setDuration(duration.toLong())
                .alpha(if (show) toAlpha else 0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = toVisibility
                    }
                })
    }
}


