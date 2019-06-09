package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState   )
        setContentView(com.example.lost.skillplus.R.layout.activity_schedule)
        setSupportActionBar(toolbar_schedule)

    skillRequest = intent.getSerializableExtra(Keys.SKILL.key) as Skill

        rV_Schedule.apply {
            layoutManager = LinearLayoutManager(this@ScheduleActivity)
            mAdapter = ScheduleAdapter(dayTimeList)
            rV_Schedule.adapter=mAdapter
        }
        val adapter = ArrayAdapter.createFromResource(this, com.example.lost.skillplus.R.array.week_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.setSelection(3, false)
        dayPicked=spinner.selectedItemPosition+1
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dayPicked = position+1
                }

        }
        val hours = findViewById<TextView>(R.id.eT_Hours)
        hours.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(this@ScheduleActivity, com.example.lost.skillplus.R.style.TimePickerTheme,
                    TimePickerDialog.OnTimeSetListener(function = { _, h, m ->
                        hours.text=h.toString()+":"+m.toString()
                        hourPicked=h
                        minutePicked=m
                    }), hour, minute, true)
            tpd.show()

        }
        btn_add_to_schedule.setOnClickListener {
            if(hourPicked==null||minutePicked==null)
            {
                Toast.makeText(this, "Please set a time first!", Toast.LENGTH_LONG).show()
            }
            else {
                if(isEmpty==true) {
                    btn_add_skill.visibility = View.VISIBLE
                    isEmpty=false
                }
                dayTimeList.add(DayTime(spinner.selectedItem.toString(), hourPicked, minutePicked))
                mAdapter.notifyDataSetChanged()
                dayTimeArray.add(arrayOf(dayPicked, hourPicked, minutePicked))

            }
        }
        btn_add_skill.setOnClickListener {
          skillRequest.schedule = NotificationAlarmManager.convertToLong(dayTimeArray)
            val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
            val call: Call<SkillsResponse>? = service?.addSkill(skillRequest)
            call?.enqueue(object : Callback<SkillsResponse> {
                override fun onResponse(call: Call<SkillsResponse>, response: Response<SkillsResponse>) {
                    if (response.isSuccessful) {
                        if(response.body()?.status  == true) { //Received response from server
                            Toast.makeText(this@ScheduleActivity,"Added Successfully",Toast.LENGTH_LONG).show()
                            //TODO COMPLETE OTHER TASK IF ANY
                            finish()
                        }
                        else{
                            Toast.makeText(this@ScheduleActivity,"Failed",Toast.LENGTH_LONG).show()

                            //Error adding in database
                        }
                    } else {
                        Toast.makeText(this@ScheduleActivity,"Failed",Toast.LENGTH_LONG).show()

                        //Error receiving response from server
                    }
                }

                override fun onFailure(call: Call<SkillsResponse>, t: Throwable) {
                    Toast.makeText(this@ScheduleActivity,"Failed",Toast.LENGTH_LONG).show()
                    //Failure sending request (Internal error)
                }

            })

        }
    }
}
