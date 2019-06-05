package com.example.lost.skillplus.views.activities

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_schedule.*
import java.util.*
import android.app.TimePickerDialog
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter

import android.widget.Toast
import com.example.lost.skillplus.models.adapters.ScheduleAdapter
import com.example.lost.skillplus.models.podos.raw.DayTime


class ScheduleActivity : AppCompatActivity() {
    private lateinit var mAdapter: ScheduleAdapter
    var dayPicked: String? = null
    var dT =DayTime("Monday",3,3)
    var dayTimeList= arrayListOf<DayTime>(dT)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.lost.skillplus.R.layout.activity_schedule)
        setSupportActionBar(toolbar)

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
                        TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                            dayTimeList.add(DayTime(dayPicked,h,m))
                            mAdapter.notifyDataSetChanged()
                        }), hour, minute, false)
                tpd.show()
            }

        }


    }
}
