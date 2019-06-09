package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
    var dayTimeList: ArrayList<DayTime> = arrayListOf()
    var dayTimeArray = arrayListOf<Array<Int?>>()
    private lateinit var skill: Skill

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.lost.skillplus.R.layout.activity_schedule)
        setSupportActionBar(toolbar_schedule)

        skill = intent.getSerializableExtra(Keys.SKILL.key) as Skill

        rV_Schedule.apply {
            layoutManager = LinearLayoutManager(this@ScheduleActivity)
            mAdapter = ScheduleAdapter(dayTimeList)
            rV_Schedule.adapter = mAdapter
        }
        val adapter = ArrayAdapter.createFromResource(this, com.example.lost.skillplus.R.array.week_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.setSelection(3, false)

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dayPicked = position + 1

                val c = Calendar.getInstance()
                val hour = c.get(Calendar.HOUR)
                val minute = c.get(Calendar.MINUTE)

                val tpd = TimePickerDialog(this@ScheduleActivity, com.example.lost.skillplus.R.style.TimePickerTheme,
                        TimePickerDialog.OnTimeSetListener(function = { _, h, m ->
                            dayTimeList.add(DayTime(spinner.selectedItem.toString(), h, m))
                            mAdapter.notifyDataSetChanged()
                            dayTimeArray.add(arrayOf(dayPicked, h, m))
                        }), hour, minute, false)
                tpd.show()
            }

        }
        btn_add_skill.setOnClickListener {
            skill.schedule = NotificationAlarmManager.convertToLong(dayTimeArray)
            val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
            val call: Call<SkillsResponse>? = service?.addSkill(skill)
            call?.enqueue(object : Callback<SkillsResponse> {
                override fun onResponse(call: Call<SkillsResponse>, response: Response<SkillsResponse>) {
                    if (response.isSuccessful) {
                        if (response.body()?.status == true) {
                            //TODO COMPLETE OTHER TASK IF ANY
                            finish()
                        } else {
                            //Error adding
                        }
                    } else {
                        //No response from retrofit
                    }
                }

                override fun onFailure(call: Call<SkillsResponse>, t: Throwable) {
                    //Failure sending request
                }

            })

        }
    }
}