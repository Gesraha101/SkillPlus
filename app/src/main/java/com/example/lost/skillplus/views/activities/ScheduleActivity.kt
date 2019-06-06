package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.app.AlarmManager
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.lost.skillplus.models.adapters.ScheduleAdapter
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.podos.raw.DayTime
import com.example.lost.skillplus.models.podos.raw.Skill
import com.example.lost.skillplus.models.podos.responses.SkillsResponse
import kotlinx.android.synthetic.main.activity_schedule.*
import org.joda.time.DateTime
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class ScheduleActivity : AppCompatActivity() {
    private lateinit var mAdapter: ScheduleAdapter
    var dayPicked: String? = null
    var dT =DayTime("Monday",3,3)
    var dayTimeList= arrayListOf<DayTime>(dT) //Todo:Intialize schedule recycler view with no data

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.lost.skillplus.R.layout.activity_schedule)
        setSupportActionBar(toolbar_schedule)

        tF_Title.text=intent.getStringExtra("skillName")

        val skillRequest = Skill(
                skill_name =intent.getStringExtra("skillName")
                ,skill_desc=intent.getStringExtra("skillDesc")
                ,session_no=intent.getIntExtra("sessionNumber",0)
                ,duration=intent.getFloatExtra("sessionDuration",0f)
                ,skill_price=intent.getFloatExtra("skillPrice",0f)
                ,extra_fees=intent.getFloatExtra("extraFees",0f)
                ,user_id=1 //Todo: get user_id from shared preferences
                ,cat_id=1 //Todo: get cat_id from ...?
                ,schedule = listOf( System.currentTimeMillis() )//Todo: Get schedule from Gesraha's ultimate equation

            ,user_name = null
            ,adding_date = null
            ,rate = null
            ,skill_id = null
        )
        val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
        val call: Call<SkillsResponse>? = service?.addSkill(skillRequest)
        call?.enqueue(object : Callback<SkillsResponse> {
            override fun onResponse(call: Call<SkillsResponse>, response: Response<SkillsResponse>) {
                if (response.isSuccessful) {
                    if(response.body()?.status  == true) { startActivity(Intent(this@ScheduleActivity, HomeActivity::class.java))}//Todo: not sure where should it go after, instead of HomeActivity
                    else{
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

        rV_Schedule.apply {
            layoutManager = LinearLayoutManager(this@ScheduleActivity)
            mAdapter = ScheduleAdapter(dayTimeList)
            rV_Schedule.adapter=mAdapter
        }
        val adapter = ArrayAdapter.createFromResource(this, com.example.lost.skillplus.R.array.week_list, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.setSelection(3, false)

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dayPicked = spinner.selectedItem.toString()

                val c = Calendar.getInstance()
                val hour = c.get(Calendar.HOUR)
                val minute = c.get(Calendar.MINUTE)

                val tpd = TimePickerDialog(this@ScheduleActivity, com.example.lost.skillplus.R.style.TimePickerTheme,
                        TimePickerDialog.OnTimeSetListener(function = { _, h, m ->
                            dayTimeList.add(DayTime(dayPicked,h,m))
                            mAdapter.notifyDataSetChanged()
                        }), hour, minute, false)
                tpd.show()
            }

        }


    }
}
