package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.TimePickerDialog
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.*
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.ScheduleAdapter
import com.example.lost.skillplus.models.enums.Keys
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.NotificationAlarmManager
import com.example.lost.skillplus.models.podos.raw.DayTime
import com.example.lost.skillplus.models.podos.raw.Skill
import com.example.lost.skillplus.models.podos.responses.SkillsResponse
import kotlinx.android.synthetic.main.activity_schedule.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ScheduleActivity : AppCompatActivity() {
    private lateinit var mAdapter: ScheduleAdapter

    var dayPicked: Int? = null
    var hourPicked: Int? = null
    var minutePicked: Int? = null
    var isEmpty: Boolean? = true
    var dayTimeList :ArrayList<DayTime> = arrayListOf()
    var dayTimeArray= arrayListOf<Array<Int?>>()
    private lateinit var skillRequest: Skill

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        setSupportActionBar(toolbar_schedule)


        var progressOverlay : View = findViewById(R.id.progress_overlay)

        skillRequest = intent.getSerializableExtra(Keys.SKILL.key) as Skill
    tF_Title.text=skillRequest.skill_name

        rV_Schedule.apply {
            layoutManager = LinearLayoutManager(this@ScheduleActivity)
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

            val tpd = TimePickerDialog(this@ScheduleActivity, R.style.TimePickerTheme,
                    TimePickerDialog.OnTimeSetListener(function = { _, h, m ->
                        hours.text = "$h:" + if (m < 10) "0$m" else "$m"
                        hourPicked=h
                        minutePicked=m
                    }), hour, minute, true)
            var date = Calendar.getInstance()
            tpd.updateTime(date.time.hours,date.time.minutes)
            tpd.show()
        }
        btn_add_to_schedule.setOnClickListener {
            if (dayPicked == null || hourPicked == null || minutePicked == null)
            {
                Toast.makeText(this, "Please set a date first!", Toast.LENGTH_LONG).show()
            }
            else {
                if(isEmpty==true) {
                    btn_add_skill.visibility = View.VISIBLE
                    isEmpty=false
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
        btn_add_skill.setOnClickListener {
            progressOverlay.visibility = View.VISIBLE
            animateView(progressOverlay, View.VISIBLE, 0.4f, 200)

            skillRequest.schedule = NotificationAlarmManager.convertToLong(dayTimeArray)
            val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
            val call: Call<SkillsResponse>? = service?.addSkill(skillRequest)
            call?.enqueue(object : Callback<SkillsResponse> {
                override fun onResponse(call: Call<SkillsResponse>, response: Response<SkillsResponse>) {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            val i = Intent(this@ScheduleActivity, HomeActivity::class.java)
                            Handler().postDelayed({
                                animateView(progressOverlay, View.GONE, 0f, 200)
                                i.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
                                i.putExtra("isComingAfterSubmition",true)
                                startActivity(i)
                                finish()
                            }, 2500)

                        }
                        else{
                            Toast.makeText(this@ScheduleActivity,"Failed",Toast.LENGTH_LONG).show()

                        }
                    } else {
                        Toast.makeText(this@ScheduleActivity,"Failed",Toast.LENGTH_LONG).show()

                        //Received response but not "OK" response i.e error in the request sent (Server can't handle this request)
                    }
                }

                override fun onFailure(call: Call<SkillsResponse>, t: Throwable) {
                    Toast.makeText(this@ScheduleActivity,"Failed",Toast.LENGTH_LONG).show()
                    //Error receiving response from server i.e error in podo received (Retrofit can't handle this response)
                }

            })

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