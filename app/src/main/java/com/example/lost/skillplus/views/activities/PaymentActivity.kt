package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.CustomAdapter
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.NotificationAlarmManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.ApplySkill
import com.example.lost.skillplus.models.podos.responses.ApplySkillResponse
import kotlinx.android.synthetic.main.activity_payment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {

    private var tv: TextView? = null
    private var scheduleList = arrayListOf<Long>()

    private var skillId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        val appliedRequest = ApplySkill(PreferencesManager(this@PaymentActivity).getId(), skillId, scheduleList)
        skillId = intent.getIntExtra("skillId", 0)

        tv = findViewById(R.id.tv)

        for (i in 0 until CustomAdapter.public_modelArrayList.size) {
            if (CustomAdapter.public_modelArrayList[i].getSelecteds()) {
                tv!!.text = tv!!.text.toString() + " , " + CustomAdapter.public_modelArrayList[i].getAnimals()
                scheduleList.add(CustomAdapter.public_modelArrayList[i].getAnimals().toLong())
            }
        }

        val share = PreferencesManager(this@PaymentActivity)
        val userId :Int = share.getId()
        val stringName = share.getName()

        if (userId != 0 && skillId != 0) {
            appliedRequest.learner = userId
            Toast.makeText(this@PaymentActivity, "cat id is " + skillId, Toast.LENGTH_LONG).show()
            appliedRequest.skill = skillId
            appliedRequest.schedule = scheduleList
        }

        cancleButton.setOnClickListener{
            startActivity(Intent(this@PaymentActivity , AddNeedActivity::class.java))
        }

        Toast.makeText(this@PaymentActivity, scheduleList.size.toString(), Toast.LENGTH_SHORT).show()
        payButton.setOnClickListener {
            if (stringName != "" && skillId != 0) {
                appliedRequest.learner = stringName!!.toInt()
                Log.d("SchaduleActivity", " id is " + userId)
                appliedRequest.skill = skillId
                Log.d("SchaduleActivity", " skillId is"+skillId.toString())
                appliedRequest.schedule = scheduleList
            }

            Log.d("SchaduleActivity", "learner"+appliedRequest.learner.toString())
            Log.d("SchaduleActivity", "skill"+appliedRequest.skill.toString())
            Log.d("SchaduleActivity" , "SchaduleActivity # "+ appliedRequest.schedule?.get(0))
            val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
            val call: Call<ApplySkillResponse>? = service?.applySkill(applySkill = appliedRequest)
            call?.enqueue(object : Callback<ApplySkillResponse> {
                override fun onResponse(call: Call<ApplySkillResponse>, response: Response<ApplySkillResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@PaymentActivity ,"you just registered in this course", Toast.LENGTH_SHORT).show()
                        for (date in scheduleList)
                            NotificationAlarmManager.initAlarm(this@PaymentActivity, date)
                    }
                    Toast.makeText(this@PaymentActivity, "learnerID = " + stringName!!.toIntOrNull() + ", skillId  " + skillId + " status " + response.body()?.status, Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<ApplySkillResponse>, t: Throwable) {
                    Toast.makeText(this@PaymentActivity, "error ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}