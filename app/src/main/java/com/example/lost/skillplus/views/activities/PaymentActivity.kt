package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.adapters.CustomAdapter
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.podos.raw.ApplySkill

import com.example.lost.skillplus.models.podos.responses.ApplySkillResponse
import com.example.lost.skillplus.models.managers.PreferencesManager
import kotlinx.android.synthetic.main.activity_payment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {

    private var tv: TextView? = null
    private var scheduleList = arrayListOf<Long>()

    private  var categoryId : Int = 0

//    private var userId :Int = 2

    private var appliedRequest  = ApplySkill(2, categoryId,scheduleList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        categoryId = intent.getSerializableExtra("CategoryId") as Int

        tv = findViewById(R.id.tv)

        for (i in 0 until CustomAdapter.public_modelArrayList.size) {
            if (CustomAdapter.public_modelArrayList[i].getSelecteds()) {
                tv!!.text = tv!!.text.toString() + " , " + CustomAdapter.public_modelArrayList[i].getAnimals()
                scheduleList.add(CustomAdapter.public_modelArrayList[i].getAnimals().toLong())
            }
        }


        val share = PreferencesManager(this@PaymentActivity)
//        val userId = share.getId()
        val stringName = share.getName()

        appliedRequest.learner = stringName!!.toInt()
     //   Toast.makeText(this@PaymentActivity ,steingName.toInt(), Toast.LENGTH_LONG)
        appliedRequest.skill = categoryId 
        appliedRequest.schedule = scheduleList



        Toast.makeText(this@PaymentActivity ,scheduleList.size.toString() , Toast.LENGTH_SHORT).show()
        payButton.setOnClickListener{
            val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
            val call: Call<ApplySkillResponse>? = service?.applySkill(applySkill = appliedRequest)
            call?.enqueue(object : Callback<ApplySkillResponse> {
                override fun onFailure(call: Call<ApplySkillResponse>, t: Throwable) {
                    Toast.makeText(this@PaymentActivity ,"Failed   ya hamada " + t.message  , Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<ApplySkillResponse>, response: Response<ApplySkillResponse>) {
//                    Toast.makeText(this@PaymentActivity ,""+ response.body()?.status , Toast.LENGTH_SHORT).show()
                   Toast.makeText(this@PaymentActivity ,"learnerID = "  +stringName.toIntOrNull()+  ", categoryId  " +categoryId + " status " + response.body()?.status, Toast.LENGTH_LONG).show()


                }
            })



        }
    }
}