package com.example.lost.skillplus.views.activities

import RetrofitManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.lost.skillplus.R
import com.example.lost.skillplus.models.managers.BackendServiceManager
import com.example.lost.skillplus.models.managers.PreferencesManager
import com.example.lost.skillplus.models.podos.raw.AddNeed
import com.example.lost.skillplus.models.podos.responses.AddNeedResponse
import kotlinx.android.synthetic.main.activity_add_form.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddNeedActivity : AppCompatActivity() {
    var category: Int = 0
    var addneed = AddNeed("" ,"" ,"" , 0 ,0)

            override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_add_student_need)
        val personNames = arrayOf("entertainment", "arts", "food")
        if (spinner != null) {
            val arrayAdapter = ArrayAdapter(this, R.layout.spiner_layout, personNames)
            spinner.adapter = arrayAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    Toast.makeText(this@AddNeedActivity, " position is " + (position + 1) + " " + personNames[position], Toast.LENGTH_SHORT).show()
                    category = position + 1
                    Log.d("need" , "category is "+category.toString())
                    addneed = AddNeed(need_name = "b3mel molokhia",
                            need_desc = "basheak sharkah el shief el shepeny",
                            need_photo = "mesh batkeshef 3la banat",
                            cat_id = category,
                            user_id = 5)
                    Log.d("need" , "addneed.cat_id is "+addneed.cat_id.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }
        val share = PreferencesManager(this@AddNeedActivity)
        val userId: Int = share.getId().toInt()

            btn_add_need.setOnClickListener {

                val service = RetrofitManager.getInstance()?.create(BackendServiceManager::class.java)
                val call: Call<AddNeedResponse>? = service?.addNeed(addneed)
                call?.enqueue(object : Callback<AddNeedResponse> {
                    override fun onFailure(call: Call<AddNeedResponse>, t: Throwable) {
                        Toast.makeText(this@AddNeedActivity, "Failed " + t.cause, Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(call: Call<AddNeedResponse>, response: Response<AddNeedResponse>) {

                        val i = Intent(this@AddNeedActivity, HomeActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        Snackbar.make(it.rootView.rootView.rootView, "Added Successfully !", Snackbar.LENGTH_INDEFINITE).show()
                        Handler().postDelayed({
                            startActivity(i)
                            finish()
                        }, 3500)


                    }

                })
            }
        }
    }
//TODO
//add pic  and conect it to need fragment

