package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.podos.raw.AddNeed
import com.example.lost.skillplus.models.podos.responses.AddNeedResponce
import com.example.lost.skillplus.models.podos.shared
import com.example.lost.skillplus.models.retrofit.ServiceManager
import kotlinx.android.synthetic.main.activity_add_need.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddNeedActivity : AppCompatActivity() {
    var category: Int = 0
    var addneed = AddNeed("" ,"" ,"" , 0 ,0)

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_need)
        val personNames = arrayOf("entertainment", "arts", "food")
        val spinner = findViewById<Spinner>(R.id.categorySpinner)
        if (spinner != null) {
            val arrayAdapter = ArrayAdapter(this, R.layout.spiner_layout, personNames)
            spinner.adapter = arrayAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    Toast.makeText(this@AddNeedActivity, " position is " + (position + 1) + " " + personNames[position], Toast.LENGTH_SHORT).show()
                    category = position + 1
                    Log.d("need" , "category is "+category.toString())
                    addneed = AddNeed(name = "b3mel molokhia",
                            desc = "basheak sharkah el shief el shepeny",
                            pic = "mesh batkeshef 3la banat",
                            cat_id = category,
                            user_id = 5)
                    Log.d("need" , "addneed.cat_id is "+addneed.cat_id.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }
        val share = shared(this@AddNeedActivity)
        val userId: Int = share.getId().toInt()

            btn_add_need.setOnClickListener {

                val service = RetrofitManager.getInstance()?.create(ServiceManager::class.java)
                val call: Call<AddNeedResponce>? = service?.addNeed(addneed)
                call?.enqueue(object : Callback<AddNeedResponce> {
                    override fun onFailure(call: Call<AddNeedResponce>, t: Throwable) {
                        Toast.makeText(this@AddNeedActivity, "Failed " + t.cause, Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(call: Call<AddNeedResponce>, response: Response<AddNeedResponce>) {

                        Toast.makeText(this@AddNeedActivity, "Done ", Toast.LENGTH_LONG).show()

                    }

                })
            }
        }
    }

//TODO
//add pic  and conect it to need fragment

