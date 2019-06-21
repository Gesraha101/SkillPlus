package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.helpers.adapters.CustomAdapter
import com.example.lost.skillplus.helpers.enums.Keys
import com.example.lost.skillplus.helpers.managers.BackendServiceManager
import com.example.lost.skillplus.helpers.managers.NotificationAlarmManager
import com.example.lost.skillplus.helpers.managers.PreferencesManager
import com.example.lost.skillplus.helpers.podos.raw.ApplySkill
import com.example.lost.skillplus.helpers.podos.raw.Schedule
import com.example.lost.skillplus.helpers.podos.raw.Skill
import com.example.lost.skillplus.helpers.podos.responses.ApplySkillResponse
import kotlinx.android.synthetic.main.activity_payment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {

    private var scheduleList = arrayListOf<Long>()
    private var skill: Skill? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        var appliedRequest: ApplySkill
        skill = intent.getSerializableExtra(Keys.SKILL.key) as Skill

        Log.d("SchaduleActivity", "skill from intent " + skill.toString())
        Log.d("SchaduleActivity", "LERNER ID  " + PreferencesManager(this@PaymentActivity).getId().toString())


        for (i in 0 until CustomAdapter.public_modelArrayList.size) {
            if (CustomAdapter.public_modelArrayList[i].getSelecteds()) {
                scheduleList.add(CustomAdapter.public_modelArrayList[i].getSchedule().toLong())
            }
        }
        Toast.makeText(this@PaymentActivity, scheduleList.size.toString(), Toast.LENGTH_SHORT).show()

        payButton.setOnClickListener {

            appliedRequest = ApplySkill(PreferencesManager(this@PaymentActivity).getId(), skill!!.skill_id!!, scheduleList)

            Log.d("SchaduleActivity", "learner  " + appliedRequest.learner.toString())
            Log.d("SchaduleActivity", "skill  " + appliedRequest.skill.toString())
            Log.d("SchaduleActivity", "SchaduleActivity # " + appliedRequest.schedule?.get(0))
            val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)

            val call: Call<ApplySkillResponse>? = service?.applySkill(appliedRequest)
            call?.enqueue(object : Callback<ApplySkillResponse> {

                override fun onResponse(call: Call<ApplySkillResponse>, response: Response<ApplySkillResponse>) {
                    if (response.isSuccessful) {
                        // Toast.makeText(this@PaymentActivity ,"you just registered in this course", Toast.LENGTH_SHORT).show()
                        for (date in scheduleList) {
                            PreferencesManager(this@PaymentActivity).addToSchedules(Schedule(date, skill!!.user_id, false))
                            NotificationAlarmManager.initAlarm(this@PaymentActivity, date, skill!!.user_id, PreferencesManager(this@PaymentActivity).getId())
                        }
                        startActivity(Intent(this@PaymentActivity, HomeActivity::class.java).addFlags(FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK))
                    }
                }

                override fun onFailure(call: Call<ApplySkillResponse>, t: Throwable) {
                    Toast.makeText(this@PaymentActivity, "error ${t.cause}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}