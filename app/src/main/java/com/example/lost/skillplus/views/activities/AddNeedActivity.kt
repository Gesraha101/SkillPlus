package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.podos.raw.AddNeed
import com.example.lost.skillplus.models.podos.responses.AddNeedResponce
import com.example.lost.skillplus.models.retrofit.ServiceManager
import kotlinx.android.synthetic.main.activity_add_need.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_need)
        val addNeed = AddNeed(name = "b3mel molokhia",
                desc = "basheak sharkah el shief el shepeny",
                pic = "mesh batkeshef 3la banat",
                cat_id = 1,
                user_Id = 5)
        btn_add_need.setOnClickListener {
            val service = RetrofitManager.getInstance()?.create(ServiceManager::class.java)
            val call: Call<AddNeedResponce>? = service?.addNeed(addNeed)
            call?.enqueue(object : Callback<AddNeedResponce> {
                override fun onFailure(call: Call<AddNeedResponce>, t: Throwable) {
                    Toast.makeText(this@AddNeedActivity, "Failed" + t.message, Toast.LENGTH_LONG).show()
                }
                override fun onResponse(call: Call<AddNeedResponce>, response: Response<AddNeedResponce>) {

                    Toast.makeText(this@AddNeedActivity, "Done ", Toast.LENGTH_LONG).show()

                }

            })
        }
    }
}
