package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.CustomAdapter
import com.example.lost.skillplus.models.podos.raw.ApplySkill

import com.example.lost.skillplus.models.podos.responses.ApplySkillResponse
import com.example.lost.skillplus.models.podos.shared
import com.example.lost.skillplus.models.retrofit.ServiceManager
import kotlinx.android.synthetic.main.activity_payment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {


    private var tv: TextView? = null
    var schadualList = arrayListOf<Long>()

    private var SkillId: Int = 0

//    private var userId :Int = 2

    private var appliedRequest = ApplySkill(2, SkillId, schadualList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        SkillId = intent.getIntExtra("SkillId", 0)

        tv = findViewById(R.id.tv) as TextView

        for (i in 0 until CustomAdapter.public_modelArrayList!!.size) {
            if (CustomAdapter.public_modelArrayList!!.get(i).getSelecteds()) {
                tv!!.text = tv!!.text.toString() + " , " + CustomAdapter.public_modelArrayList!!.get(i).getAnimals()
                schadualList.add(CustomAdapter.public_modelArrayList!!.get(i).getAnimals().toLong())
            }
        }

        val share = shared(this@PaymentActivity)
        val userId :Int = share.getId().toInt()
        val steingName = share.getName()

        if (userId != 0 && SkillId != 0) {
            appliedRequest.learner = userId
            Toast.makeText(this@PaymentActivity, "cat id is " + SkillId, Toast.LENGTH_LONG)
            appliedRequest.skill = SkillId
            appliedRequest.schedule = schadualList
        }

        cancleButton.setOnClickListener{
            startActivity(Intent(this@PaymentActivity , AddNeedActivity::class.java))
        }

        Toast.makeText(this@PaymentActivity, schadualList.size.toString(), Toast.LENGTH_SHORT).show()
        payButton.setOnClickListener {
            if (steingName != "" && SkillId != 0) {
                appliedRequest.learner = steingName.toInt()
                Log.d("schadual", " id is " + userId)
                appliedRequest.skill = SkillId
                Log.d("schadual", " SkillId is"+SkillId.toString())
                appliedRequest.schedule = schadualList
            }

            Log.d("schadual", "learner"+appliedRequest.learner.toString())
            Log.d("schadual", "skill"+appliedRequest.skill.toString())
            Log.d("schadual" , "schadual # "+ appliedRequest.schedule?.get(0))
            val service = RetrofitManager.getInstance()?.create(ServiceManager::class.java)
            val call: Call<ApplySkillResponse>? = service?.applySkill(applySkill = appliedRequest)
            call?.enqueue(object : Callback<ApplySkillResponse> {
                override fun onFailure(call: Call<ApplySkillResponse>, t: Throwable) {
                       Toast.makeText(this@PaymentActivity ,"you just registerd in this course "   , Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<ApplySkillResponse>, response: Response<ApplySkillResponse>) {
                    Toast.makeText(this@PaymentActivity, "learnerID = " + steingName.toIntOrNull() + ", SkillId  " + SkillId + " status " + response.body()?.status, Toast.LENGTH_LONG).show()


                }
            })


        }
    }
}