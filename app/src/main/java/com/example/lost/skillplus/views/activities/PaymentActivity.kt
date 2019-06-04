package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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

    private  var categoryId : Int = 0

//    private var userId :Int = 2

    private var appliedRequest  = ApplySkill(2, categoryId,schadualList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        categoryId = intent.getSerializableExtra("CategoryId") as Int

        tv = findViewById(R.id.tv) as TextView

        for (i in 0 until CustomAdapter.public_modelArrayList!!.size) {
            if (CustomAdapter.public_modelArrayList!!.get(i).getSelecteds()) {
                tv!!.text = tv!!.text.toString() + " , " + CustomAdapter.public_modelArrayList!!.get(i).getAnimals()
                schadualList.add(CustomAdapter.public_modelArrayList!!.get(i).getAnimals().toLong())
            }
        }


        val share = shared(this@PaymentActivity)
//        val userId = share.getId()
        val steingName = share.getName()

        appliedRequest.learner = steingName.toInt()!!
     //   Toast.makeText(this@PaymentActivity ,steingName.toInt(), Toast.LENGTH_LONG)
        appliedRequest.skill = categoryId 
        appliedRequest.schedule = schadualList



        Toast.makeText(this@PaymentActivity ,schadualList.size.toString() , Toast.LENGTH_SHORT).show()
        payButton.setOnClickListener{
            val service = RetrofitManager.getInstance()?.create(ServiceManager::class.java)
            val call: Call<ApplySkillResponse>? = service?.applySkill(applySkill = appliedRequest)
            call?.enqueue(object : Callback<ApplySkillResponse> {
                override fun onFailure(call: Call<ApplySkillResponse>, t: Throwable) {
                    Toast.makeText(this@PaymentActivity ,"Failed   ya hamada " + t.message  , Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<ApplySkillResponse>, response: Response<ApplySkillResponse>) {
//                    Toast.makeText(this@PaymentActivity ,""+ response.body()?.status , Toast.LENGTH_SHORT).show()
                   Toast.makeText(this@PaymentActivity ,"learnerID = "  +steingName.toIntOrNull()+  ", categoryId  " +categoryId + " status " + response.body()?.status, Toast.LENGTH_LONG).show()


                }
            })



        }
    }
}